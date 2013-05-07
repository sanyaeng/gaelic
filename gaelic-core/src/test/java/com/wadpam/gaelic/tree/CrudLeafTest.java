/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wadpam.gaelic.tree;

import com.wadpam.gaelic.*;
import com.wadpam.gaelic.json.JCursorPage;
import com.wadpam.gaelic.json.JDate;
import java.io.IOException;
import javax.servlet.ServletException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;

import static org.junit.Assert.*;

/**
 *
 * @author os
 */
public class CrudLeafTest {
    static final Logger LOG = LoggerFactory.getLogger(GaelicServletTest.class);
    
    MockHttpServletRequest request;
    MockHttpServletResponse response;
    GaelicServlet servlet;
    
    public CrudLeafTest() {
        MockServletConfig servletConfig = new MockServletConfig("gaelic-test");
        servletConfig.addInitParameter(GaelicServlet.INIT_PARAM_CONFIG, UnitTestConfig.class.getName());
        servlet = new GaelicServlet();
        try {
            servlet.init(servletConfig);
        } catch (ServletException ex) {
            LOG.error("<init>", ex);
        }
    }
    
    @Before
    public void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }
    
    @After
    public void tearDown() {
        LOG.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    }
    
    protected JDate doCreate() throws ServletException, IOException {
        request.setMethod("POST");
        request.setRequestURI("/api/gaelic/crud/v10");
        request.setContentType("application/json");
        request.setContent("{\"startDate\":12345678}".getBytes());
        
        servlet.service(request, response);
        return GaelicServlet.MAPPER.readValue(response.getContentAsString(), JDate.class);
    }

    @Test
    public void testCreate()  throws ServletException, IOException {
        LOG.info("---------------- testCreate() -------------------------------");

        JDate jDate = doCreate();
        
        assertEquals(201, response.getStatus());
        assertNotNull(response.getContentAsString());
        assertEquals("12345678", jDate.getId());
        assertEquals((Long)12345678L, jDate.getStartDate());

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        request.setMethod("GET");
        request.setRequestURI(String.format("/api/gaelic/crud/v10/%s", jDate.getId()));
        
        servlet.service(request, response);
        assertEquals(200, response.getStatus());
    }
    
    @Test
    public void testDelete()  throws ServletException, IOException {
        LOG.info("---------------- testDelete() -------------------------------");

        JDate jDate = doCreate();
        assertEquals(201, response.getStatus());

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        request.setMethod("DELETE");
        request.setRequestURI(String.format("/api/gaelic/crud/v10/%s", jDate.getId()));
        
        servlet.service(request, response);
        assertEquals(200, response.getStatus());

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        request.setMethod("GET");
        request.setRequestURI(String.format("/api/gaelic/crud/v10/%s", jDate.getId()));
        
        servlet.service(request, response);
        assertEquals(404, response.getStatus());
    }
    
    @Test
    public void testGetPage() throws ServletException, IOException {
        JDate jDate = doCreate();
        assertEquals(201, response.getStatus());

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        request.setMethod("GET");
        request.setRequestURI("/api/gaelic/crud/v10");
        LOG.info("---------------- testGetPage() -------------------------------");

        servlet.service(request, response);
        assertEquals(200, response.getStatus());
        
        assertNull(request.getAttribute(CrudLeaf.REQUEST_ATTR_FILENAME));
        
        assertNotNull(response.getContentAsString());
        JDatePage page = GaelicServlet.MAPPER.readValue(response.getContentAsByteArray(), JDatePage.class);
        assertNull(page.getCursorKey());
        assertEquals(1, page.getItems().size());
        assertEquals(1, page.getTotalSize().intValue());
    }

    @Test
    public void testGetPageEmptyFilename() throws ServletException, IOException {
        request.setMethod("GET");
        request.setRequestURI("/api/gaelic/crud/v10/");
        LOG.info("---------------- testGetPageEmptyFilename() ------------------");

        servlet.service(request, response);
        assertEquals(200, response.getStatus());
        
        Node handler = (Node) request.getAttribute(GaelicServlet.REQUEST_ATTR_HANDLERNODE);
        assertNotNull(handler);
        String domain = handler.getPathVariable("domain");
        assertEquals("gaelic", domain);
        
        assertNull(request.getAttribute(CrudLeaf.REQUEST_ATTR_FILENAME));
    }

    @Test
    public void testGetDetailsEmptyFilename() throws ServletException, IOException {
        JDate expected = doCreate();
        
        request.setMethod("GET");
        request.setRequestURI(String.format("/api/gaelic/crud/v10/%s/", expected.getId()));
        LOG.info("---------------- testGetDetailsEmptyFilename() ------------------");

        servlet.service(request, response);
        assertEquals(200, response.getStatus());
        
        Node handler = (Node) request.getAttribute(GaelicServlet.REQUEST_ATTR_HANDLERNODE);
        assertNotNull(handler);
        String domain = handler.getPathVariable("domain");
        assertEquals("gaelic", domain);
        
        assertEquals(expected.getId(), request.getAttribute(CrudLeaf.REQUEST_ATTR_FILENAME));
        Object body = request.getAttribute(GaelicServlet.REQUEST_ATTR_RESPONSEBODY);
        assertNotNull(body);
        assertTrue(body instanceof JDate);
        JDate jDate = (JDate) body;
        assertEquals(expected.getId(), jDate.getId());
    }

    @Test
    public void testGetDetails() throws ServletException, IOException {
        JDate expected = doCreate();
        
        request.setMethod("GET");
        request.setRequestURI(String.format("/api/gaelic/crud/v10/%s", expected.getId()));
        LOG.info("---------------- testGetDetails() ------------------");

        servlet.service(request, response);
        assertEquals(200, response.getStatus());
        
        Node handler = (Node) request.getAttribute(GaelicServlet.REQUEST_ATTR_HANDLERNODE);
        assertNotNull(handler);
        String domain = handler.getPathVariable("domain");
        assertEquals("gaelic", domain);
        
        assertEquals(expected.getId(), request.getAttribute(CrudLeaf.REQUEST_ATTR_FILENAME));
        Object body = request.getAttribute(GaelicServlet.REQUEST_ATTR_RESPONSEBODY);
        assertNotNull(body);
        assertTrue(body instanceof JDate);
        JDate jDate = (JDate) body;
        assertEquals(expected.getId(), jDate.getId());
    }

    @Test
    public void testNotFound() throws ServletException, IOException {
        request.setMethod("GET");
        request.setRequestURI("/api/anything/crud/v10/24/parent");
        LOG.info("---------------- testNotFound() ------------------------------");

        servlet.service(request, response);
        assertEquals(404, response.getStatus());
        assertEquals("Not Found", response.getErrorMessage());
        assertTrue(response.getContentAsString().startsWith(
                "{\"code\":0,\"status\":404,\"message\":\"Not Found\",\"stackTrace\":\"com.wadpam.gaelic.exception.RestException.<clinit>:"));
    }

    @Test
    public void testIdNotLong() throws ServletException, IOException {
        request.setMethod("GET");
        request.setRequestURI("/api/anything/crud/v10/notLong");
        LOG.info("---------------- testIdNotLong() ------------------------------");

        servlet.service(request, response);
        assertEquals(400, response.getStatus());
        assertEquals("Bad Request", response.getErrorMessage());
        LOG.info(response.getContentAsString());
        assertTrue(response.getContentAsString().startsWith(
                "{\"code\":3,\"status\":400,\"message\":\"Bad Request\",\"developerMessage\":\"notLong\",\"stackTrace\":\"com.wadpam.gaelic.tree.CrudLeaf.getId:"));
    }

    @Test
    public void testMethodNotAllowed()  throws ServletException, IOException {
        request.setMethod("PUT");
        request.setRequestURI("/api/gaelic/crud/v10");
        request.setQueryString("_expects=201");
        request.setContentType("application/json");
        request.setContent("{\"startDate\":12345678}".getBytes());
        LOG.info("---------------- testMethodNotAllowed() -------------------------------");

        servlet.service(request, response);
        assertEquals(405, response.getStatus());
        assertEquals("DELETE, GET, POST", response.getHeader("Allow"));
        assertNotNull(response.getContentAsString());
    }
    
    @Test
    public void testUpdate()  throws ServletException, IOException {
        LOG.info("---------------- testUpdate() -------------------------------");

        JDate jDate = doCreate();
        assertEquals(201, response.getStatus());

        final long millis = System.currentTimeMillis();
        assertTrue(jDate.getStartDate() < millis);
        jDate.setStartDate(millis);
        
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        request.setMethod("POST");
        request.setRequestURI(String.format("/api/gaelic/crud/v10/%s", jDate.getId()));
        request.setContentType("application/json");
        byte[] requestBody = GaelicServlet.MAPPER.writeValueAsBytes(jDate);
        request.setContent(requestBody);
        
        servlet.service(request, response);
        assertEquals(200, response.getStatus());
        assertNotNull(response.getContentAsString());
        
        JDate actual = GaelicServlet.MAPPER.readValue(response.getContentAsString(), JDate.class);
        
        assertEquals(jDate.getId(), actual.getId());
        assertEquals((Long)millis, actual.getStartDate());
        
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        request.setMethod("GET");
        request.setRequestURI(String.format("/api/gaelic/crud/v10/%s", jDate.getId()));
        
        servlet.service(request, response);
        assertEquals(200, response.getStatus());
        assertNotNull(response.getContentAsString());
        
        JDate read = GaelicServlet.MAPPER.readValue(response.getContentAsString(), JDate.class);
        
        assertEquals(jDate.getId(), read.getId());
        assertEquals((Long)millis, read.getStartDate());
    }

    static final class JDatePage extends JCursorPage<JDate> {}
}