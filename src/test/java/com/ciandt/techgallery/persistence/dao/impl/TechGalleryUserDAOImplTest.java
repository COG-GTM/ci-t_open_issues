package com.ciandt.techgallery.persistence.dao.impl;

import static org.junit.Assert.*;

import com.ciandt.techgallery.persistence.model.TechGalleryUser;
import com.ciandt.techgallery.service.BaseServiceTest;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TechGalleryUserDAOImplTest extends BaseServiceTest {

    private TechGalleryUserDAOImpl dao;
    
    @Before
    public void setUp() throws Exception {
        super.setUpBase();
        dao = TechGalleryUserDAOImpl.getInstance();
    }

    @Test
    public void testGetInstance() {
        TechGalleryUserDAOImpl instance1 = TechGalleryUserDAOImpl.getInstance();
        TechGalleryUserDAOImpl instance2 = TechGalleryUserDAOImpl.getInstance();
        
        assertNotNull(instance1);
        assertSame(instance1, instance2);
    }

    @Test
    public void testFindByLogin_NotFound() {
        TechGalleryUser result = dao.findByLogin("nonexistent@example.com");
        
        assertNull(result);
    }

    @Test
    public void testFindByLogin_Found() {
        TechGalleryUser user = createTestUser("test@example.com", "123");
        dao.add(user);
        
        TechGalleryUser result = dao.findByLogin("test@example.com");
        
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    public void testFindByGoogleId_NotFound() {
        TechGalleryUser result = dao.findByGoogleId("nonexistent");
        
        assertNull(result);
    }

    @Test
    public void testFindByGoogleId_Found() {
        TechGalleryUser user = createTestUser("test@example.com", "123");
        dao.add(user);
        
        TechGalleryUser result = dao.findByGoogleId("123");
        
        assertNotNull(result);
        assertEquals("123", result.getGoogleId());
    }

    @Test
    public void testFindByEmail_NotFound() {
        TechGalleryUser result = dao.findByEmail("nonexistent@example.com");
        
        assertNull(result);
    }

    @Test
    public void testFindByEmail_Found() {
        TechGalleryUser user = createTestUser("test@example.com", "123");
        dao.add(user);
        
        TechGalleryUser result = dao.findByEmail("test@example.com");
        
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    public void testFindByNameAndEmail_NotFound() {
        TechGalleryUser result = dao.findByNameAndEmail("Test User", "nonexistent@example.com");
        
        assertNull(result);
    }

    @Test
    public void testFindByNameAndEmail_Found() {
        TechGalleryUser user = createTestUser("test@example.com", "123");
        user.setName("Test User");
        dao.add(user);
        
        TechGalleryUser result = dao.findByNameAndEmail("Test User", "test@example.com");
        
        assertNotNull(result);
        assertEquals("Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    public void testFindAllFollowers_EmptyList() {
        List<TechGalleryUser> result = dao.findAllFollowers();
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindAllFollowers_WithFollowers() {
        TechGalleryUser user = createTestUser("test@example.com", "123");
        List<String> followedTechs = new ArrayList<>();
        followedTechs.add("java");
        followedTechs.add("python");
        user.setFollowedTechnologyIds(followedTechs);
        dao.add(user);
        
        TechGalleryUser userWithoutFollows = createTestUser("test2@example.com", "456");
        dao.add(userWithoutFollows);
        
        List<TechGalleryUser> result = dao.findAllFollowers();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test@example.com", result.get(0).getEmail());
    }
}
