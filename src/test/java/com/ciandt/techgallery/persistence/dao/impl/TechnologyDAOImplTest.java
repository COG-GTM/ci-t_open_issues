package com.ciandt.techgallery.persistence.dao.impl;

import static org.junit.Assert.*;

import com.ciandt.techgallery.persistence.model.Technology;
import com.ciandt.techgallery.service.BaseServiceTest;

import org.junit.Before;
import org.junit.Test;

public class TechnologyDAOImplTest extends BaseServiceTest {

    private TechnologyDAOImpl dao;
    
    @Before
    public void setUp() throws Exception {
        super.setUpBase();
        dao = TechnologyDAOImpl.getInstance();
    }

    @Test
    public void testGetInstance() {
        TechnologyDAOImpl instance1 = TechnologyDAOImpl.getInstance();
        TechnologyDAOImpl instance2 = TechnologyDAOImpl.getInstance();
        
        assertNotNull(instance1);
        assertSame(instance1, instance2);
    }

    @Test
    public void testFindByName_NotFound() {
        Technology result = dao.findByName("NonExistentTechnology");
        
        assertNull(result);
    }

    @Test
    public void testFindByIdActive_NotFound() {
        Technology result = dao.findByIdActive("nonexistent");
        
        assertNull(result);
    }

    @Test
    public void testFindByIdActive_InactiveTechnology() {
        Technology tech = createTestTechnology("java", "Java");
        tech.setActive(false);
        dao.add(tech);
        
        Technology result = dao.findByIdActive("java");
        
        assertNull(result);
    }

    @Test
    public void testFindByIdActive_ActiveTechnology() {
        Technology tech = createTestTechnology("java", "Java");
        tech.setActive(true);
        dao.add(tech);
        
        Technology result = dao.findByIdActive("java");
        
        assertNotNull(result);
        assertEquals("java", result.getId());
        assertTrue(result.getActive());
    }

    @Test
    public void testFindByName_Found() {
        Technology tech = createTestTechnology("java", "Java");
        dao.add(tech);
        
        Technology result = dao.findByName("Java");
        
        assertNotNull(result);
        assertEquals("Java", result.getName());
        assertEquals("java", result.getId());
    }
}
