package com.ciandt.techgallery.service.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.users.User;

import com.ciandt.techgallery.persistence.dao.TechnologyDAO;
import com.ciandt.techgallery.persistence.dao.StorageDAO;
import com.ciandt.techgallery.persistence.model.TechGalleryUser;
import com.ciandt.techgallery.persistence.model.Technology;
import com.ciandt.techgallery.service.BaseServiceTest;
import com.ciandt.techgallery.service.UserServiceTG;
import com.ciandt.techgallery.service.enums.ValidationMessageEnums;
import com.ciandt.techgallery.service.model.Response;
import com.ciandt.techgallery.service.model.TechnologiesResponse;
import com.ciandt.techgallery.service.model.TechnologyFilter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TechnologyServiceImplTest extends BaseServiceTest {

    @Mock
    private TechnologyDAO mockTechnologyDAO;
    
    @Mock
    private UserServiceTG mockUserService;
    
    @Mock
    private StorageDAO mockStorageDAO;
    
    @Mock
    private User mockUser;
    
    private TechnologyServiceImpl service;
    
    @Before
    public void setUp() throws Exception {
        super.setUpBase();
        MockitoAnnotations.initMocks(this);
        service = TechnologyServiceImpl.getInstance();
        
        Field technologyDAOField = TechnologyServiceImpl.class.getDeclaredField("technologyDAO");
        technologyDAOField.setAccessible(true);
        technologyDAOField.set(service, mockTechnologyDAO);
        
        Field userServiceField = TechnologyServiceImpl.class.getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(service, mockUserService);
        
        Field storageDAOField = TechnologyServiceImpl.class.getDeclaredField("storageDAO");
        storageDAOField.setAccessible(true);
        storageDAOField.set(service, mockStorageDAO);
    }

    @Test
    public void testConvertNameToId() throws Exception {
        java.lang.reflect.Method method = TechnologyServiceImpl.class.getDeclaredMethod("convertNameToId", String.class);
        method.setAccessible(true);
        
        String result = (String) method.invoke(service, "Java Spring Framework");
        assertEquals("java_spring_framework", result);
        
        result = (String) method.invoke(service, "Node.js");
        assertEquals("node.js", result);
        
        result = (String) method.invoke(service, "C++");
        assertEquals("c++", result);
    }

    @Test
    public void testValidateInformations_Success() throws Exception {
        Technology tech = createTestTechnology("java", "Java");
        when(mockTechnologyDAO.findByName("Java")).thenReturn(null);
        
        java.lang.reflect.Method method = TechnologyServiceImpl.class.getDeclaredMethod("validateInformations", Technology.class);
        method.setAccessible(true);
        
        Technology result = (Technology) method.invoke(service, tech);
        assertNull(result);
    }

    @Test(expected = BadRequestException.class)
    public void testValidateInformations_BlankId() throws Exception {
        Technology tech = createTestTechnology("", "Java");
        
        java.lang.reflect.Method method = TechnologyServiceImpl.class.getDeclaredMethod("validateInformations", Technology.class);
        method.setAccessible(true);
        
        try {
            method.invoke(service, tech);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    @Test(expected = BadRequestException.class)
    public void testValidateInformations_BlankName() throws Exception {
        Technology tech = createTestTechnology("java", "");
        
        java.lang.reflect.Method method = TechnologyServiceImpl.class.getDeclaredMethod("validateInformations", Technology.class);
        method.setAccessible(true);
        
        try {
            method.invoke(service, tech);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    @Test(expected = BadRequestException.class)
    public void testValidateInformations_BlankShortDescription() throws Exception {
        Technology tech = createTestTechnology("java", "Java");
        tech.setShortDescription("");
        
        java.lang.reflect.Method method = TechnologyServiceImpl.class.getDeclaredMethod("validateInformations", Technology.class);
        method.setAccessible(true);
        
        try {
            method.invoke(service, tech);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    @Test(expected = BadRequestException.class)
    public void testValidateInformations_BlankDescription() throws Exception {
        Technology tech = createTestTechnology("java", "Java");
        tech.setDescription("");
        
        java.lang.reflect.Method method = TechnologyServiceImpl.class.getDeclaredMethod("validateInformations", Technology.class);
        method.setAccessible(true);
        
        try {
            method.invoke(service, tech);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    @Test(expected = BadRequestException.class)
    public void testValidateInformations_NameAlreadyExists() throws Exception {
        Technology tech = createTestTechnology(null, "Java");
        Technology existingTech = createTestTechnology("java", "Java");
        when(mockTechnologyDAO.findByName("Java")).thenReturn(existingTech);
        
        java.lang.reflect.Method method = TechnologyServiceImpl.class.getDeclaredMethod("validateInformations", Technology.class);
        method.setAccessible(true);
        
        try {
            method.invoke(service, tech);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    @Test
    public void testGetTechnologies_Success() throws Exception {
        List<Technology> technologies = new ArrayList<>();
        technologies.add(createTestTechnology("java", "Java"));
        technologies.add(createTestTechnology("python", "Python"));
        
        when(mockTechnologyDAO.findAllActives()).thenReturn(technologies);
        when(mockUser.getUserId()).thenReturn("123");
        
        TechGalleryUser user = createTestUser("test@example.com", "123");
        when(mockUserService.getUserByGoogleId("123")).thenReturn(user);
        
        Response response = service.getTechnologies(mockUser);
        
        assertNotNull(response);
        assertTrue(response instanceof TechnologiesResponse);
        TechnologiesResponse techResponse = (TechnologiesResponse) response;
        assertEquals(2, techResponse.getTechnologies().size());
    }

    @Test
    public void testGetTechnologies_EmptyList() throws Exception {
        when(mockTechnologyDAO.findAllActives()).thenReturn(new ArrayList<Technology>());
        
        Response response = service.getTechnologies(mockUser);
        
        assertNotNull(response);
        assertTrue(response instanceof TechnologiesResponse);
    }

    @Test
    public void testGetTechnologies_NullList() throws Exception {
        when(mockTechnologyDAO.findAllActives()).thenReturn(null);
        
        Response response = service.getTechnologies(mockUser);
        
        assertNotNull(response);
        assertTrue(response instanceof TechnologiesResponse);
    }

    @Test
    public void testGetTechnologyById_Success() throws Exception {
        Technology tech = createTestTechnology("java", "Java");
        when(mockTechnologyDAO.findByIdActive("java")).thenReturn(tech);
        when(mockUser.getUserId()).thenReturn("123");
        
        TechGalleryUser user = createTestUser("test@example.com", "123");
        when(mockUserService.getUserByGoogleId("123")).thenReturn(user);
        
        Technology result = service.getTechnologyById("java", mockUser);
        
        assertNotNull(result);
        assertEquals("java", result.getId());
        assertEquals("Java", result.getName());
    }

    @Test(expected = NotFoundException.class)
    public void testGetTechnologyById_NotFound() throws Exception {
        when(mockTechnologyDAO.findByIdActive("nonexistent")).thenReturn(null);
        
        service.getTechnologyById("nonexistent", mockUser);
    }

    @Test
    public void testAddCommentariesCounter() throws Exception {
        Technology tech = createTestTechnology("java", "Java");
        tech.setCommentariesCounter(5);
        
        service.addCommentariesCounter(tech);
        
        assertEquals(Integer.valueOf(6), tech.getCommentariesCounter());
        verify(mockTechnologyDAO).update(tech);
    }

    @Test
    public void testAddCommentariesCounter_NullEntity() throws Exception {
        service.addCommentariesCounter(null);
        
        verify(mockTechnologyDAO).update(null);
    }

    @Test
    public void testRemoveCommentariesCounter() throws Exception {
        Technology tech = createTestTechnology("java", "Java");
        tech.setCommentariesCounter(5);
        
        service.removeCommentariesCounter(tech);
        
        assertEquals(Integer.valueOf(4), tech.getCommentariesCounter());
        verify(mockTechnologyDAO).update(tech);
    }

    @Test
    public void testAddRecomendationCounter_Positive() throws Exception {
        Technology tech = createTestTechnology("java", "Java");
        tech.setPositiveRecommendationsCounter(3);
        
        service.addRecomendationCounter(tech, true);
        
        assertEquals(Integer.valueOf(4), tech.getPositiveRecommendationsCounter());
        verify(mockTechnologyDAO).update(tech);
    }

    @Test
    public void testAddRecomendationCounter_Negative() throws Exception {
        Technology tech = createTestTechnology("java", "Java");
        tech.setNegativeRecommendationsCounter(2);
        
        service.addRecomendationCounter(tech, false);
        
        assertEquals(Integer.valueOf(3), tech.getNegativeRecommendationsCounter());
        verify(mockTechnologyDAO).update(tech);
    }

    @Test
    public void testAddRecomendationCounter_NullEntity() throws Exception {
        service.addRecomendationCounter(null, true);
        
        verify(mockTechnologyDAO, never()).update(any(Technology.class));
    }

    @Test
    public void testRemoveRecomendationCounter_Positive() throws Exception {
        Technology tech = createTestTechnology("java", "Java");
        tech.setPositiveRecommendationsCounter(3);
        
        service.removeRecomendationCounter(tech, true);
        
        assertEquals(Integer.valueOf(2), tech.getPositiveRecommendationsCounter());
        verify(mockTechnologyDAO).update(tech);
    }

    @Test
    public void testUpdateEdorsedsCounter() throws Exception {
        Technology tech = createTestTechnology("java", "Java");
        
        service.updateEdorsedsCounter(tech, 10);
        
        assertEquals(Integer.valueOf(10), tech.getEndorsersCounter());
        verify(mockTechnologyDAO).update(tech);
    }

    @Test(expected = BadRequestException.class)
    public void testValidateUser_NullUser() throws Exception {
        java.lang.reflect.Method method = TechnologyServiceImpl.class.getDeclaredMethod("validateUser", User.class);
        method.setAccessible(true);
        
        try {
            method.invoke(service, (User) null);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    @Test(expected = BadRequestException.class)
    public void testValidateUser_NullUserId() throws Exception {
        when(mockUser.getUserId()).thenReturn(null);
        
        java.lang.reflect.Method method = TechnologyServiceImpl.class.getDeclaredMethod("validateUser", User.class);
        method.setAccessible(true);
        
        try {
            method.invoke(service, mockUser);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    @Test(expected = NotFoundException.class)
    public void testValidateUser_UserNotExists() throws Exception {
        when(mockUser.getUserId()).thenReturn("123");
        when(mockUserService.getUserByGoogleId("123")).thenReturn(null);
        
        java.lang.reflect.Method method = TechnologyServiceImpl.class.getDeclaredMethod("validateUser", User.class);
        method.setAccessible(true);
        
        try {
            method.invoke(service, mockUser);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    @Test
    public void testGetOrderOptions() throws Exception {
        List<String> options = service.getOrderOptions(mockUser);
        
        assertNotNull(options);
        assertFalse(options.isEmpty());
    }
}
