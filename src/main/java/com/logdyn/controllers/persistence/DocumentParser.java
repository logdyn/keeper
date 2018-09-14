package com.logdyn.controllers.persistence;

import com.logdyn.core.repository.Repository;
import com.logdyn.core.repository.RepositoryController;
import com.logdyn.core.task.Task;
import com.logdyn.core.task.WorkLog;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class DocumentParser {

    private static final Logger LOGGER = Logger.getLogger(DocumentParser.class);
    private final Document doc;

    public DocumentParser(final Document doc) {
        this.doc = doc;
    }

    public Collection<Repository> parse(){

        Element root = doc.getDocumentElement();

        Node repositories = root.getElementsByTagName("repositories").item(0);

        List<Repository> repos = new ArrayList<>(repositories.getChildNodes().getLength());

        for(Node node = repositories.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (node instanceof Element) {
                try {
                    repos.add(parseRepoElement((Element) node));
                } catch (MalformedURLException e) {
                    LOGGER.error("Could not parse repository URL", e);
                }
            }
        }

        return repos;
    }

    private Repository parseRepoElement(Element element) throws MalformedURLException {

        String name = element.getElementsByTagName("name").item(0).getTextContent();
        URL url = new URL(element.getElementsByTagName("url").item(0).getTextContent());

        Optional<Repository> repo = RepositoryController.addRepository(url, name);
        repo.ifPresent(repository -> {
            NodeList tasksNodeList = element.getElementsByTagName("tasks");
            if (tasksNodeList.getLength() > 0) {

                Element tasks = (Element) tasksNodeList.item(0);

                for(Node node = tasks.getFirstChild(); node != null; node = node.getNextSibling()) {
                    if (node instanceof Element) {
                        try {
                            repository.addTask(parseTaskElement((Element) node));
                        } catch (MalformedURLException e) {
                            LOGGER.error("Could not parse task URL", e);
                        }
                    }
                }
            }

        });

        //! Returns null as repos are added as part of parse, should be returned later
        return null;
    }

    private Task parseTaskElement(final Element element) throws MalformedURLException {

        final String id = element.getAttribute("id");
        final String title = element.getElementsByTagName("title").item(0).getTextContent();
        final String description = element.getElementsByTagName("description").item(0).getTextContent();
        final URL url = new URL(element.getElementsByTagName("url").item(0).getTextContent());

        final WorkLog worklog = parseWorklogElement((Element) element.getElementsByTagName("worklog").item(0));

        return new Task(id, title, description, url, worklog);
    }

    private WorkLog parseWorklogElement(final Element element) {
        final String comment = element.getElementsByTagName("comment").item(0).getTextContent();
        final long start = Long.parseLong(element.getElementsByTagName("start").item(0).getTextContent());
        final long duration = Long.parseLong(element.getElementsByTagName("duration").item(0).getTextContent());
        return new WorkLog(start, duration, comment);
    }
}
