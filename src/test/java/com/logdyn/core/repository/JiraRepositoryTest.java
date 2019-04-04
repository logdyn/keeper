package com.logdyn.core.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.logdyn.core.authentication.AuthenticationRequiredException;
import com.logdyn.core.repository.dto.JiraWorkLog;
import com.logdyn.core.task.Task;
import com.logdyn.core.task.WorkLog;
import com.logdyn.core.task.timer.SingleTimer;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class JiraRepositoryTest
{

    @Test
    void submitTask() throws IOException, AuthenticationRequiredException
    {
        final Random random = new Random();
        final WorkLog workLog = new WorkLog(new SingleTimer(List.of(0L, random.nextLong())), Integer.toString(random.nextInt(), 36));
        final JiraWorkLog jiraworkLog = new JiraWorkLog(workLog);
        final HttpServer httpServer = HttpServer.create(new InetSocketAddress(0), 0);
        httpServer.createContext("/", exchange -> {
            exchange.sendResponseHeaders(201, 0);

            final JsonNode json = new ObjectMapper().readTree(exchange.getRequestBody());
            assertEquals(jiraworkLog.getStarted(),json.get("started").asText());
            assertEquals(jiraworkLog.getTimeSpentSeconds(), json.get("timeSpentSeconds").asLong());
            assertEquals(jiraworkLog.getComment(), json.get("comment").asText());

            exchange.close();
        });
        httpServer.start();

        final JiraRepository jiraRepository = new JiraRepository(
                new URL("http://localhost:"+httpServer.getAddress().getPort()),
                null);
        jiraRepository.submitTask(new Task(null, null, null, null, workLog));

        httpServer.stop(1);
    }
}
