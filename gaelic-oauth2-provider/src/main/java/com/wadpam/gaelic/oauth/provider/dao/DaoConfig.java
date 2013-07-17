package com.wadpam.gaelic.oauth.provider.dao;

import java.util.HashMap;
import java.util.Map;
import net.sf.mardao.core.dao.DaoImpl;
import com.wadpam.gaelic.oauth.provider.domain.Do2pProfile;

/**
 * Context to define the Dao beans.
 * This file is generated by mardao, but edited by developers.
 * It is not overwritten by the generator once it exists.
 *
 * Generated on 2013-07-17T19:10:35.416+0700.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public class DaoConfig {

    public static Map<Class, DaoImpl> createDaos() {
        final HashMap<Class, DaoImpl> map = new HashMap<Class, DaoImpl>();

        // first, create and map Dao instances:
        final Do2pProfileDaoBean do2pProfileDao =
            new Do2pProfileDaoBean();
        map.put(Do2pProfile.class, do2pProfileDao);

        // next, wire parents;

        // finally, wire many-to-ones;
	
        return map;
    }
}