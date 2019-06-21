package com.sparrow.demo.mapper;

import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author allen
 */
@Repository
public interface SpaDocumentMapper {

    void docAdd(Map<String,Object> params);

    void updateDocMeta(Map<String,Object> params);

}
