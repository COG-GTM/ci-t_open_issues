package com.ciandt.techgallery.service.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.Ref;

import com.ciandt.techgallery.persistence.dao.SkillDAO;
import com.ciandt.techgallery.persistence.model.Skill;
import com.ciandt.techgallery.persistence.model.TechGalleryUser;
import com.ciandt.techgallery.persistence.model.Technology;
import com.ciandt.techgallery.service.BaseServiceTest;
import com.ciandt.techgallery.service.TechnologyService;
import com.ciandt.techgallery.service.UserServiceTG;
import com.ciandt.techgallery.service.enums.ValidationMessageEnums;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SkillServiceImplTest extends BaseServiceTest {

    @Mock
    private SkillDAO mockSkillDao;
    
    @Mock
    private TechnologyService mockTechService;
    
    @Mock
    private UserServiceTG mockUserService;
    
    @Mock
    private User mockUser;
    
    private SkillServiceImpl service;
    
    @Before
    public void setUp() throws Exception {
        super.setUpBase();
        MockitoAnnotations.initMocks(this);
        service = SkillServiceImpl.getInstance();
        
        Field skillDaoField = SkillServiceImpl.class.getDeclaredField("skillDao");
        skillDaoField.setAccessible(true);
        skillDaoField.set(service, mockSkillDao);
        
        Field techServiceField = SkillServiceImpl.class.getDeclaredField("techService");
        techServiceField.setAccessible(true);
        techServiceField.set(service, mockTechService);
        
        Field userServiceField = SkillServiceImpl.class.getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(service, mockUserService);
    }

    @Test
    public void testValidateInputs_Success() throws Exception {
        Skill skill = new Skill();
        skill.setValue(3);
        Technology tech = createTestTechnology("java", "Java");
        skill.setTechnology(Ref.create(tech));
        
        when(mockUser.getUserId()).thenReturn("123");
        TechGalleryUser user = createTestUser("test@example.com", "123");
        when(mockUserService.getUserByGoogleId("123")).thenReturn(user);
        
        Method method = SkillServiceImpl.class.getDeclaredMethod("validateInputs", Skill.class, User.class);
        method.setAccessible(true);
        
        method.invoke(service, skill, mockUser);
    }

    @Test(expected = BadRequestException.class)
    public void testValidateInputs_NullUser() throws Exception {
        Skill skill = new Skill();
        skill.setValue(3);
        
        Method method = SkillServiceImpl.class.getDeclaredMethod("validateInputs", Skill.class, User.class);
        method.setAccessible(true);
        
        try {
            method.invoke(service, skill, null);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    @Test(expected = BadRequestException.class)
    public void testValidateInputs_NullUserId() throws Exception {
        Skill skill = new Skill();
        skill.setValue(3);
        
        when(mockUser.getUserId()).thenReturn(null);
        
        Method method = SkillServiceImpl.class.getDeclaredMethod("validateInputs", Skill.class, User.class);
        method.setAccessible(true);
        
        try {
            method.invoke(service, skill, mockUser);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    @Test(expected = BadRequestException.class)
    public void testValidateInputs_EmptyUserId() throws Exception {
        Skill skill = new Skill();
        skill.setValue(3);
        
        when(mockUser.getUserId()).thenReturn("");
        
        Method method = SkillServiceImpl.class.getDeclaredMethod("validateInputs", Skill.class, User.class);
        method.setAccessible(true);
        
        try {
            method.invoke(service, skill, mockUser);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    @Test(expected = BadRequestException.class)
    public void testValidateInputs_UserNotExists() throws Exception {
        Skill skill = new Skill();
        skill.setValue(3);
        
        when(mockUser.getUserId()).thenReturn("123");
        when(mockUserService.getUserByGoogleId("123")).thenReturn(null);
        
        Method method = SkillServiceImpl.class.getDeclaredMethod("validateInputs", Skill.class, User.class);
        method.setAccessible(true);
        
        try {
            method.invoke(service, skill, mockUser);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    @Test(expected = BadRequestException.class)
    public void testValidateInputs_NullSkill() throws Exception {
        when(mockUser.getUserId()).thenReturn("123");
        TechGalleryUser user = createTestUser("test@example.com", "123");
        when(mockUserService.getUserByGoogleId("123")).thenReturn(user);
        
        Method method = SkillServiceImpl.class.getDeclaredMethod("validateInputs", Skill.class, User.class);
        method.setAccessible(true);
        
        try {
            method.invoke(service, null, mockUser);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    @Test(expected = BadRequestException.class)
    public void testValidateInputs_NullSkillValue() throws Exception {
        Skill skill = new Skill();
        skill.setValue(null);
        
        when(mockUser.getUserId()).thenReturn("123");
        TechGalleryUser user = createTestUser("test@example.com", "123");
        when(mockUserService.getUserByGoogleId("123")).thenReturn(user);
        
        Method method = SkillServiceImpl.class.getDeclaredMethod("validateInputs", Skill.class, User.class);
        method.setAccessible(true);
        
        try {
            method.invoke(service, skill, mockUser);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    @Test(expected = BadRequestException.class)
    public void testValidateInputs_SkillValueTooLow() throws Exception {
        Skill skill = new Skill();
        skill.setValue(-1);
        
        when(mockUser.getUserId()).thenReturn("123");
        TechGalleryUser user = createTestUser("test@example.com", "123");
        when(mockUserService.getUserByGoogleId("123")).thenReturn(user);
        
        Method method = SkillServiceImpl.class.getDeclaredMethod("validateInputs", Skill.class, User.class);
        method.setAccessible(true);
        
        try {
            method.invoke(service, skill, mockUser);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    @Test(expected = BadRequestException.class)
    public void testValidateInputs_SkillValueTooHigh() throws Exception {
        Skill skill = new Skill();
        skill.setValue(6);
        
        when(mockUser.getUserId()).thenReturn("123");
        TechGalleryUser user = createTestUser("test@example.com", "123");
        when(mockUserService.getUserByGoogleId("123")).thenReturn(user);
        
        Method method = SkillServiceImpl.class.getDeclaredMethod("validateInputs", Skill.class, User.class);
        method.setAccessible(true);
        
        try {
            method.invoke(service, skill, mockUser);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    @Test(expected = BadRequestException.class)
    public void testValidateInputs_NullTechnology() throws Exception {
        Skill skill = new Skill();
        skill.setValue(3);
        skill.setTechnology(null);
        
        when(mockUser.getUserId()).thenReturn("123");
        TechGalleryUser user = createTestUser("test@example.com", "123");
        when(mockUserService.getUserByGoogleId("123")).thenReturn(user);
        
        Method method = SkillServiceImpl.class.getDeclaredMethod("validateInputs", Skill.class, User.class);
        method.setAccessible(true);
        
        try {
            method.invoke(service, skill, mockUser);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    @Test
    public void testGetUserSkill_Success() throws Exception {
        Technology tech = createTestTechnology("java", "Java");
        TechGalleryUser user = createTestUser("test@example.com", "123");
        Skill skill = new Skill();
        skill.setValue(3);
        
        when(mockUser.getUserId()).thenReturn("123");
        when(mockUserService.getUserByGoogleId("123")).thenReturn(user);
        when(mockTechService.getTechnologyById("java", mockUser)).thenReturn(tech);
        when(mockSkillDao.findByUserAndTechnology(user, tech)).thenReturn(skill);
        
        Skill result = service.getUserSkill("java", (User) mockUser);
        
        assertNotNull(result);
        assertEquals(Integer.valueOf(3), result.getValue());
    }

    @Test(expected = OAuthRequestException.class)
    public void testGetUserSkill_NullUser() throws Exception {
        service.getUserSkill("java", (User) null);
    }

    @Test(expected = BadRequestException.class)
    public void testGetUserSkill_NullUserId() throws Exception {
        when(mockUser.getUserId()).thenReturn(null);
        
        service.getUserSkill("java", (User) mockUser);
    }

    @Test(expected = BadRequestException.class)
    public void testGetUserSkill_EmptyUserId() throws Exception {
        when(mockUser.getUserId()).thenReturn("");
        
        service.getUserSkill("java", (User) mockUser);
    }

    @Test(expected = BadRequestException.class)
    public void testGetUserSkill_UserNotExists() throws Exception {
        when(mockUser.getUserId()).thenReturn("123");
        when(mockUserService.getUserByGoogleId("123")).thenReturn(null);
        
        service.getUserSkill("java", (User) mockUser);
    }

    @Test(expected = BadRequestException.class)
    public void testGetUserSkill_TechnologyNotExists() throws Exception {
        when(mockUser.getUserId()).thenReturn("123");
        TechGalleryUser user = createTestUser("test@example.com", "123");
        when(mockUserService.getUserByGoogleId("123")).thenReturn(user);
        when(mockTechService.getTechnologyById("java", mockUser)).thenReturn(null);
        
        service.getUserSkill("java", (User) mockUser);
    }

    @Test(expected = NotFoundException.class)
    public void testGetUserSkill_SkillNotExists() throws Exception {
        Technology tech = createTestTechnology("java", "Java");
        TechGalleryUser user = createTestUser("test@example.com", "123");
        
        when(mockUser.getUserId()).thenReturn("123");
        when(mockUserService.getUserByGoogleId("123")).thenReturn(user);
        when(mockTechService.getTechnologyById("java", mockUser)).thenReturn(tech);
        when(mockSkillDao.findByUserAndTechnology(user, tech)).thenReturn(null);
        
        service.getUserSkill("java", (User) mockUser);
    }

    @Test
    public void testGetUserSkillWithTechGalleryUser_Success() throws Exception {
        Technology tech = createTestTechnology("java", "Java");
        TechGalleryUser user = createTestUser("test@example.com", "123");
        Skill skill = new Skill();
        skill.setValue(3);
        
        when(mockTechService.getTechnologyById("java", null)).thenReturn(tech);
        when(mockSkillDao.findByUserAndTechnology(user, tech)).thenReturn(skill);
        
        Skill result = service.getUserSkill("java", user);
        
        assertNotNull(result);
        assertEquals(Integer.valueOf(3), result.getValue());
    }

    @Test(expected = OAuthRequestException.class)
    public void testGetUserSkillWithTechGalleryUser_NullUser() throws Exception {
        service.getUserSkill("java", (TechGalleryUser) null);
    }

    @Test(expected = NotFoundException.class)
    public void testGetUserSkillWithTechGalleryUser_TechnologyNotExists() throws Exception {
        TechGalleryUser user = createTestUser("test@example.com", "123");
        when(mockTechService.getTechnologyById("java", null)).thenReturn(null);
        
        service.getUserSkill("java", user);
    }

    @Test
    public void testGetUserSkillWithTechGalleryUser_SkillNotExists() throws Exception {
        Technology tech = createTestTechnology("java", "Java");
        TechGalleryUser user = createTestUser("test@example.com", "123");
        
        when(mockTechService.getTechnologyById("java", null)).thenReturn(tech);
        when(mockSkillDao.findByUserAndTechnology(user, tech)).thenReturn(null);
        
        Skill result = service.getUserSkill("java", user);
        
        assertNull(result);
    }
}
