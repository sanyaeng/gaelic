/*
 * INSERT COPYRIGHT HERE
 */

package com.wadpam.gaelic.tree;

import com.wadpam.gaelic.converter.MardaoConverter;
import com.wadpam.gaelic.crud.CrudService;
import com.wadpam.gaelic.domain.DAppDomain;
import com.wadpam.gaelic.json.JAppDomain;

/**
 *
 * @author sosandstrom
 */
public class AppDomainLeaf extends CrudLeaf<JAppDomain, DAppDomain, String, CrudService<DAppDomain, String>> {
    
    protected static final AppDomainConverter CONVERTER = new AppDomainConverter();

    public AppDomainLeaf(Class domainClass, Class idClass, Class jsonClass) {
        super(domainClass, idClass, jsonClass);
        setConverter(CONVERTER);
    }

    static class AppDomainConverter extends MardaoConverter<JAppDomain, DAppDomain> {

        public AppDomainConverter() {
            super(JAppDomain.class, DAppDomain.class);
        }
        
        @Override
        public void convertDomain(DAppDomain from, JAppDomain to) {
            convertDAppDomain(from, to);
        }
        
        public static void convertDAppDomain(DAppDomain from, JAppDomain to) {
            convertStringEntity(from, to);
            
            to.setAnalyticsTrackingCode(from.getAnalyticsTrackingCode());
            to.setAppArg1(from.getAppArg1());
            to.setAppArg2(from.getAppArg2());
            to.setDescription(from.getDescription());
            to.setEmail(from.getEmail());
            to.setPassword(from.getPassword());
            to.setUsername(from.getUsername());
        }

        @Override
        public void convertJson(JAppDomain from, DAppDomain to) {
            convertJAppDomain(from, to);
        }
        
        public static void convertJAppDomain(JAppDomain from, DAppDomain to) {
            convertJString(from, to);
            
            to.setAnalyticsTrackingCode(from.getAnalyticsTrackingCode());
            to.setAppArg1(from.getAppArg1());
            to.setAppArg2(from.getAppArg2());
            to.setDescription(from.getDescription());
            to.setEmail(from.getEmail());
            to.setPassword(from.getPassword());
            to.setUsername(from.getUsername());
        }
    }
}
