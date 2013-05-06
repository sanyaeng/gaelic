/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wadpam.gaelic.crud;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.wadpam.gaelic.*;
import com.wadpam.gaelic.tree.CrudLeaf;
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
public class MardaoCrudTest {
    static final Logger LOG = LoggerFactory.getLogger(MardaoCrudTest.class);

    final LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig()
                .setDefaultHighRepJobPolicyUnappliedJobPercentage(10));
    
    MockHttpServletRequest request;
    MockHttpServletResponse response;
    GaelicServlet servlet;
    
    public MardaoCrudTest() {
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
        helper.setUp();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        LOG.info("--------------------------------------------------------------");
    }
    
    @After
    public void tearDown() {
        LOG.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        helper.tearDown();
    }
    
//    @Test
//    public void testCreate()  throws ServletException, IOException {
//        request.setMethod("POST");
//        request.setRequestURI("/api/gaelic/crud/v10?_expects=201");
//        request.setContentType("application/json");
//        request.setContent("{\"startDate\":12345678}".getBytes());
//        LOG.info("---------------- testCreate() -------------------------------");
//
//        servlet.service(request, response);
//        assertEquals(201, response.getStatus());
//        
//        Node handler = (Node) request.getAttribute(GaelicServlet.REQUEST_ATTR_HANDLERNODE);
//        assertNotNull(handler);
//        String domain = handler.getPathVariable("domain");
//        assertEquals("gaelic", domain);
//        
//        assertNotNull(response.getContentAsString());
//    }

    @Test
    public void testGetPage() throws ServletException, IOException {
        request.setMethod("GET");
        request.setRequestURI("/api/gaelic/crud/v10");
        LOG.info("---------------- testGetPage() -------------------------------");

        servlet.service(request, response);
        assertEquals(200, response.getStatus());
        
        Node handler = (Node) request.getAttribute(GaelicServlet.REQUEST_ATTR_HANDLERNODE);
        assertNotNull(handler);
        String domain = handler.getPathVariable("domain");
        assertEquals("gaelic", domain);
        
        assertNull(request.getAttribute(CrudLeaf.REQUEST_ATTR_FILENAME));
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
        request.setMethod("GET");
        request.setRequestURI("/api/gaelic/crud/v10/42/");
        LOG.info("---------------- testGetDetailsEmptyFilename() ------------------");

        servlet.service(request, response);
        assertEquals(404, response.getStatus());
        
        Node handler = (Node) request.getAttribute(GaelicServlet.REQUEST_ATTR_HANDLERNODE);
        assertNotNull(handler);
        String domain = handler.getPathVariable("domain");
        assertEquals("gaelic", domain);
        
        assertEquals("42", request.getAttribute(CrudLeaf.REQUEST_ATTR_FILENAME));
        assertNull(request.getAttribute(GaelicServlet.REQUEST_ATTR_RESPONSEBODY));
    }

    @Test
    public void testGetDetails() throws ServletException, IOException {
        request.setMethod("GET");
        request.setRequestURI("/api/gaelic/crud/v10/42");
        LOG.info("---------------- testGetDetails() ------------------");

        servlet.service(request, response);
        assertEquals(404, response.getStatus());
        
        Node handler = (Node) request.getAttribute(GaelicServlet.REQUEST_ATTR_HANDLERNODE);
        assertNotNull(handler);
        String domain = handler.getPathVariable("domain");
        assertEquals("gaelic", domain);
        
        assertEquals("42", request.getAttribute(CrudLeaf.REQUEST_ATTR_FILENAME));
        assertNull(request.getAttribute(GaelicServlet.REQUEST_ATTR_RESPONSEBODY));
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
        assertTrue(response.getContentAsString().startsWith(
                "{\"code\":3,\"status\":400,\"message\":\"Bad Request\",\"developerMessage\":\"notLong\",\"stackTrace\":\"com.wadpam.gaelic.tree.CrudLeaf.getDetails:"));
    }

}