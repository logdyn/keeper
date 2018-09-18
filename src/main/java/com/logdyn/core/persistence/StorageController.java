package com.logdyn.core.persistence;

import com.logdyn.core.persistence.adapters.Adapter;
import com.logdyn.core.persistence.adapters.NullAdapter;
import com.logdyn.core.repository.Repository;
import com.logdyn.core.repository.RepositoryController;
import com.logdyn.core.task.WorkLog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.Collection;

public final class StorageController {
    private static final int VERSION;
    private static final String FILEPATH = System.getProperty("user.home") + "/.keeper/state.xml"; //NON-NLS
    private static final Collection<Adapter> adapters = new ArrayList<>();

    static {
        adapters.add(new NullAdapter(adapters.size()));

        VERSION = adapters.size();
    }

    private StorageController() {
        throw new AssertionError();
    }

    public static void save() {
        final File file = new File(FILEPATH);
        if (!file.getParentFile().exists() && !file.getParentFile().mkdir()) {
            throw new UncheckedIOException(new FileSystemException(file.getParent(), null, "could not create folder"));
        }
        final Document doc = StorageController.createDocument();
        StorageController.write(doc, file);
    }

    private static void write(final Document doc, final File file) {
        try {
            final TransformerFactory tFactory = TransformerFactory.newInstance();
            final Transformer transformer = tFactory.newTransformer();
            final DOMSource source = new DOMSource(doc);
            final StreamResult result = new StreamResult(new FileOutputStream(file));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //NON-NLS
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2"); //NON-NLS
            transformer.transform(source, result);

        } catch (final TransformerException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void open() {
        final File file = new File(FILEPATH);
        final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        try {
            final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(file);
            doc = StorageController.adapt(doc);
            final Collection<Repository> repositories = new DocumentParser(doc).parse();
            //! Parser adds repositories, could change
            //RepositoryController.addRepositories(repositories);
        } catch (final ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    private static Document adapt(final Document document) {
        return adapters.stream()
                .sorted()
                .sequential()
                .reduce(document, (doc, adapter) -> adapter.adapt(doc), (doc1, doc2) -> doc1);
    }

    private static Document createDocument() {
        final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        try {
            final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            final Document doc = docBuilder.newDocument();

            final Element root = doc.createElement("state"); //NON-NLS
            doc.appendChild(root);

            root.setAttribute("version", String.valueOf(VERSION)); //NON-NLS

            final Element repositories = doc.createElement("repositories"); //NON-NLS
            root.appendChild(repositories);

            RepositoryController.getRepositories().forEach(repo -> {
                final Element repository = doc.createElement("repository"); //NON-NLS
                repositories.appendChild(repository);

                appendTextNode("name", repo.getName(), repository, doc); //NON-NLS
                appendTextNode("url", repo.getUrl().toString(), repository, doc); //NON-NLS

                final Element tasks = doc.createElement("tasks"); //NON-NLS
                repository.appendChild(tasks);

                repo.getTasks().forEach(t -> {
                    final Element task = doc.createElement("task"); //NON-NLS
                    tasks.appendChild(task);

                    task.setAttribute("id", t.getId()); //NON-NLS
                    appendTextNode("title", t.getTitle(), task, doc); //NON-NLS
                    appendTextNode("description", t.getDescription(), task, doc); //NON-NLS
                    appendTextNode("url", t.getURL().toString(), task,doc); //NON-NLS

                    final Element worklog = doc.createElement("worklog"); //NON-NLS
                    task.appendChild(worklog);

                    final WorkLog log = t.getCurrentWorkLog();
                    appendTextNode("comment", log.getComment(), worklog, doc); //NON-NLS
                    appendTextNode("start", String.valueOf(log.getTimer().getStartTime()), worklog, doc); //NON-NLS
                    appendTextNode("duration", String.valueOf(log.getTimer().getDuration()), worklog, doc); //NON-NLS
                });
            });

            return doc;

        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private static Node appendTextNode(final String tagName, final String value, final Node parent, final Document doc) {
        final Node node = doc.createElement(tagName);
        node.appendChild(doc.createTextNode(value));
        return parent.appendChild(node);
    }
}