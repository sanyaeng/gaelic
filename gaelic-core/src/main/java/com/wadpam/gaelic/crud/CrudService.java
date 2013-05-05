package com.wadpam.gaelic.crud;

import com.wadpam.gaelic.json.JCursorPage;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author os
 */
public interface CrudService<T, 
        ID extends Serializable> {
    
    /**
     * Factory method that creates a domain-model side object.
     * @return 
     */
    T createDomain();
    
    /**
     * Persists an entity in the underlying database.
     * @param domain
     * @return the generated ID of the entity.
     */
    ID create(T domain);
    
    /**
     * Deletes an entity from the underlying database persistence.
     * @param parentKeyString
     * @param id 
     */
    void delete(String parentKeyString, ID id);

    /**
     * Deletes specified entities from the underlying database persistence.
     * @param parentKeyString
     * @param id an array of IDs
     */
    void delete(String parentKeyString, ID[] id);

    /**
     * Reads the specified entity from underlying database persistence.
     * @param parentKeyString
     * @param id
     * @return null if not found, otherwise the read entity.
     */
    T get(String parentKeyString, ID id);
    
    /**
     * Batch-reads specified entities from underlying database persistence.
     * @param ids
     * @return 
     */
    Iterable<T> getByPrimaryKeys(Collection<ID> ids);

    Class getDaoClass();
    Class<T> getDomainClass();
    Class<ID> getIdClass();
    
    /**
     * Reads a page of entities from the underlying database persistence.
     * @param pageSize
     * @param cursorKey null for first page, otherwise same value as returned in previous CursorPage
     * @return 
     */
    JCursorPage<T> getPage(int pageSize, String cursorKey);
    
    /**
     * Extractor method to get the simple key from specified domain entity object.
     * @param domain
     * @return 
     */
    ID getSimpleKey(T domain);
    
    /**
     * Extractor method to get the simple key toString from specified domain primary key.
     * @param primaryKey
     * @return 
     */
    String getSimpleKeyString(Object primaryKey);
    
    /**
     * Extractor method to get the parent key string from specified domain entity object.
     * @param domain
     * @return 
     */
    String getParentKeyString(T domain);

    /**
     * Returns the name of the primary key property / column.
     * @return 
     */
    String getPrimaryKeyColumnName();
    
    /**
     * @return the class of the primary key, e.g. Long or String
     */
    Class getPrimaryKeyColumnClass();
    
    /**
     * @return the table name or entity kind
     */
    String getTableName();
    
    Map<String, Class> getTypeMap();
    
    /**
     * Updates an entity in the underlying datastore persistence.
     * @param domain
     * @return the ID of the updated entity
     */
    ID update(T domain);
    
    /**
     * Updates or Inserts specified entities in the underlying datastore persistence.
     * @param domains
     * @return the IDs of the upserted entities
     */
    List<ID> upsert(Iterable<T> domains);
    
    /**
     * Returns a page of IDs, for those entities that has changed or been deleted since.
     * @param since include entities that has changed or been deleted after this value
     * @param pageSize
     * @param cursorKey null for first page, otherwise same value as returned in previous CursorPage
     * @return the IDs of the upserted entities
     */
    JCursorPage<ID> whatsChanged(Date since, int pageSize, String cursorKey);

}
