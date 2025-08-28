package com.ciandt.techgallery.service;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import com.googlecode.objectify.ObjectifyService;
import com.ciandt.techgallery.persistence.model.TechGalleryUser;
import com.ciandt.techgallery.persistence.model.Technology;
import com.ciandt.techgallery.persistence.model.Skill;
import com.ciandt.techgallery.persistence.model.Endorsement;

import org.junit.After;
import org.junit.Before;

public abstract class BaseServiceTest {

    protected final LocalServiceTestHelper helper = 
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Before
    public void setUpBase() {
        helper.setUp();
        ObjectifyService.register(TechGalleryUser.class);
        ObjectifyService.register(Technology.class);
        ObjectifyService.register(Skill.class);
        ObjectifyService.register(Endorsement.class);
        ObjectifyService.begin();
    }

    @After
    public void tearDownBase() {
        helper.tearDown();
    }

    protected Technology createTestTechnology(String id, String name) {
        Technology tech = new Technology();
        tech.setId(id);
        tech.setName(name);
        tech.setShortDescription("Test short description");
        tech.setDescription("Test description");
        tech.setActive(true);
        tech.initCounters();
        return tech;
    }

    protected TechGalleryUser createTestUser(String email, String googleId) {
        TechGalleryUser user = new TechGalleryUser();
        user.setEmail(email);
        user.setGoogleId(googleId);
        user.setName("Test User");
        return user;
    }
}
