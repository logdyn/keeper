package com.logdyn.controllers.persistence;

import com.logdyn.model.repositories.Repository;
import org.w3c.dom.Document;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public class DocumentParser {
    private enum Model {
        REPOSITORY("repository"),
        TASK("task"),
        WORKLOG("worklog");
        private String tagName;
        Model(final String tagName) {
            this.tagName = tagName;
        }

        public String getTagName() {
            return this.tagName;
        }

        public static Optional<Model> fromTagName(final String tagName){
            return Arrays.stream(Model.values())
                    .filter(m -> m.getTagName().equals(tagName))
                    .findAny();
        }
    }

    private Model currentModel = null;

    public DocumentParser(final File file) {
        this.file = file;
    }

    private File file;

    public Collection<Repository> parse(){
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = factory.createXMLEventReader(new FileInputStream(this.file));
        while(eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            switch(event.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:{
                    StartElement startElement = event.asStartElement();
                    final String tagName = startElement.getName().getLocalPart();
                    Model.fromTagName(tagName)
                            .ifPresent(m -> this.currentModel = m);
                    break;
                }
                case XMLStreamConstants.CHARACTERS: {
                    //TODO builders
                    break;
                }
                case XMLStreamConstants.END_ELEMENT: {
                    EndElement endElement = event.asEndElement();
                    final String tagName = endElement.getName().getLocalPart();
                    Model.fromTagName(tagName)
                            .filter(this.currentModel::equals)
                            .ifPresent(m -> this.currentModel = null);
                }
            }
        }

        return null;
    }
}
