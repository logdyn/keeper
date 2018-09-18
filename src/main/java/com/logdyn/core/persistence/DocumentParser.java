package com.logdyn.core.persistence;

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

        final Element root = doc.getDocumentElement();

        final Node repositories = root.getElementsByTagName("repositories").item(0); //NON-NLS

        final List<Repository> repos = new ArrayList<>(repositories.getChildNodes().getLength());

        for(Node node = repositories.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (node instanceof Element) {
                try {
                    repos.add(this.parseRepoElement((Element) node));
                } catch (final MalformedURLException e) {
                    LOGGER.error("Could not parse repository URL", e); //NON-NLS
                }
            }
        }

        return repos;
    }

    private Repository parseRepoElement(final Element element) throws MalformedURLException {

        final String name = this.getTagValue(element,"name"); //NON-NLS
        final URL url = new URL(this.getTagValue(element,"url")); //NON-NLS

        final Optional<Repository> repo = RepositoryController.addRepository(url, name);
        repo.ifPresent(repository -> {
            final NodeList tasksNodeList = element.getElementsByTagName("tasks"); //NON-NLS
            if (tasksNodeList.getLength() > 0) {

                final Element tasks = (Element) tasksNodeList.item(0);

                for(Node node = tasks.getFirstChild(); node != null; node = node.getNextSibling()) {
                    if (node instanceof Element) {
                        try {
                            repository.addTask(this.parseTaskElement((Element) node));
                        } catch (MalformedURLException e) {
                            LOGGER.error("Could not parse task URL", e); //NON-NLS
                        }
                    }
                }
            }

        });

        //! Returns null as repos are added as part of parse, should be returned later
        return null;
    }

    private Task parseTaskElement(final Element element) throws MalformedURLException {

        final String id = element.getAttribute("id"); //NON-NLS
        final String title = this.getTagValue(element,"title"); //NON-NLS
        final String description = this.getTagValue(element,"description"); //NON-NLS
        final URL url = new URL(this.getTagValue(element,"url")); //NON-NLS

        final WorkLog worklog = parseWorklogElement((Element) element.getElementsByTagName("worklog").item(0)); //NON-NLS

        return new Task(id, title, description, url, worklog);
    }

    private WorkLog parseWorklogElement(final Element element) {
        final String comment = this.getTagValue(element,"comment"); //NON-NLS
        final long start = Long.parseLong(this.getTagValue(element,"start")); //NON-NLS
        final long duration = Long.parseLong(this.getTagValue(element,"duration")); //NON-NLS
        return new WorkLog(start, duration, comment);
    }

    private String getTagValue(final Element element, final String tagName) {
        return element.getElementsByTagName(tagName).item(0).getTextContent();
    }
}
