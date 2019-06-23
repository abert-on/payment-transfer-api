package com.revolut.util;

import org.apache.commons.io.FileUtils;
import spark.utils.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class TestUtils {

    public static String getExpectedBody(Class clazz, String filename) throws IOException {
        URL url = clazz.getResource(filename);
        return FileUtils.readFileToString(new File(url.getPath()), StandardCharsets.UTF_8);
    }

    public static TestResponse request(String method, String path) throws IOException {
        return request(method, path, null);
    }

    public static TestResponse request(String method, String path, String body) throws IOException {
        URL url = new URL("http://localhost:4567" + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setDoOutput(true);
        addRequestBody(connection, body);
        connection.connect();
        return new TestResponse(connection.getResponseCode(), getResponseBody(connection));
    }

    private static void addRequestBody(HttpURLConnection connection, String body) throws IOException {
        if (body != null) {
            try(OutputStream outputStream = connection.getOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
                writer.write(body);
                writer.flush();
            }

        }
    }

    private static String getResponseBody(HttpURLConnection connection) throws IOException {
        String body;
        if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 400) {
            body = IOUtils.toString(connection.getInputStream());
        }
        else {
            body = IOUtils.toString(connection.getErrorStream());
        }
        return body;
    }

    public static class TestResponse {
        public String body;
        public int status;

        public TestResponse(int status, String body) {
            this.body = body;
            this.status = status;
        }
    }
}
