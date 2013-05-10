/*
 * INSERT COPYRIGHT HERE
 */

package com.wadpam.gaelic.crud;

import com.wadpam.gaelic.json.JCursorPage;
import com.wadpam.gaelic.tree.CrudLeaf;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author sosandstrom
 */
public class CrudListenerAdapter<J extends Serializable, T, ID extends Serializable, S extends CrudService<T, ID>>
        implements CrudListener<J, T, ID, S> {

    @Override
    public void preService(CrudLeaf<J, T, ID, S> controller, 
            S service, 
            HttpServletRequest request, 
            String namespace, 
            int operation, 
            Object json, 
            Object domain, 
            Serializable id) {
        switch (operation) {
            case CREATE:
                preCreate(controller, service, request, namespace, json, domain);
                break;
            case GET:
                preGet(controller, service, request, namespace, id);
                break;
            case UPDATE:
                preUpdate(controller, service, request, namespace, json, domain, id);
                break;
            case DELETE:
                preDelete(controller, service, request, namespace, id);
                break;
            case GET_PAGE:
                break;
            case WHAT_CHANGED:
                break;
            case UPSERT_BATCH:
                break;
            case DELETE_BATCH:
                break;
            case GET_EXISTING:
                break;
        }
    }

    @Override
    public void postService(CrudLeaf<J, T, ID, S> controller, 
            S service, 
            HttpServletRequest request, 
            String namespace, 
            int operation, 
            Object json, 
            Serializable id, 
            Object serviceResponse) {
        switch (operation) {
            case CREATE:
                postCreate(controller, service, request, namespace, json, id, serviceResponse);
                break;
            case GET:
                postGet(controller, service, request, namespace, id, serviceResponse, json);
                break;
            case UPDATE:
                postUpdate(controller, service, request, namespace, json, id, serviceResponse);
                break;
            case DELETE:
                postDelete(controller, service, request, namespace, id);
                break;
            case GET_PAGE:
                postGetPage(controller, service, request, namespace, (JCursorPage<J>) json, (String) id, (JCursorPage<T>) serviceResponse);
                break;
            case WHAT_CHANGED:
                break;
            case UPSERT_BATCH:
                break;
            case DELETE_BATCH:
                break;
            case GET_EXISTING:
                break;
        }
    }

    /**
     * Override to implement the pre-service create callback
     * @param controller
     * @param service
     * @param request
     * @param namespace
     * @param json
     * @param domain
     */
    protected void preCreate(CrudLeaf controller, CrudService service, HttpServletRequest request, String namespace, Object json, Object domain) {
    }

    protected void preGet(CrudLeaf controller, CrudService service, HttpServletRequest request, String namespace, Serializable id) {
    }

    protected void preUpdate(CrudLeaf controller, CrudService service, HttpServletRequest request, String namespace, Object json, Object domain, Serializable id) {
    }

    protected void preDelete(CrudLeaf controller, CrudService service, HttpServletRequest request, String namespace, Serializable id) {
    }
    
    /**
     * Override to implement the post-service create callback
     * @param controller
     * @param service
     * @param request
     * @param domain
     * @param json
     * @param id
     * @param serviceResponse 
     */
    protected void postCreate(CrudLeaf controller, CrudService service, HttpServletRequest request, String domain, Object json, Serializable id, Object serviceResponse) {
    }

    /**
     * 
     * @param controller
     * @param service
     * @param request
     * @param namespace
     * @param id
     * @param serviceResponse
     * @param jsonResponse 
     */
    protected void postGet(CrudLeaf controller, CrudService service, HttpServletRequest request, 
            String namespace, Serializable id, Object serviceResponse, Object jsonResponse) {
    }

    protected void postUpdate(CrudLeaf controller, CrudService service, HttpServletRequest request, 
            String namespace, Object json, Serializable id, Object serviceResponse) {
    }

    protected void postDelete(CrudLeaf controller, CrudService service, HttpServletRequest request, 
            String namespace, Serializable id) {
    }

    protected void postGetPage(CrudLeaf<J, T, ID, S> controller, S service, HttpServletRequest request, 
            String namespace, JCursorPage<J> jPage, String cursorKey, JCursorPage<T> serviceResponse) {
    }

}