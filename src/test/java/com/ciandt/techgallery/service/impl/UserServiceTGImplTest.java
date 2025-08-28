package com.ciandt.techgallery.service.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.users.User;

import com.ciandt.techgallery.persistence.dao.TechGalleryUserDAO;
import com.ciandt.techgallery.persistence.model.TechGalleryUser;
import com.ciandt.techgallery.service.BaseServiceTest;
import com.ciandt.techgallery.service.enums.ValidationMessageEnums;
import com.ciandt.techgallery.service.model.Response;
import com.ciandt.techgallery.service.model.UsersResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class UserServiceTGImplTest extends BaseServiceTest {

    @Mock
    private TechGalleryUserDAO mockUserDao;
    
    @Mock
    private User mockUser;
    
    private UserServiceTGImpl service;
    
    @Before
    public void setUp() throws Exception {
        super.setUpBase();
        MockitoAnnotations.initMocks(this);
        service = UserServiceTGImpl.getInstance();
        
        Field userDaoField = UserServiceTGImpl.class.getDeclaredField("userDao");
        userDaoField.setAccessible(true);
        userDaoField.set(service, mockUserDao);
    }

    @Test
    public void testGetUsers_Success() throws Exception {
        List<TechGalleryUser> users = new ArrayList<>();
        users.add(createTestUser("user1@example.com", "123"));
        users.add(createTestUser("user2@example.com", "456"));
        
        when(mockUserDao.findAll()).thenReturn(users);
        
        Response response = service.getUsers();
        
        assertNotNull(response);
        assertTrue(response instanceof UsersResponse);
        UsersResponse usersResponse = (UsersResponse) response;
        assertEquals(2, usersResponse.getUsers().size());
    }

    @Test(expected = NotFoundException.class)
    public void testGetUsers_NullList() throws Exception {
        when(mockUserDao.findAll()).thenReturn(null);
        
        service.getUsers();
    }

    @Test
    public void testGetUser_Success() throws Exception {
        TechGalleryUser user = createTestUser("test@example.com", "123");
        user.setId(1L);
        when(mockUserDao.findById(1L)).thenReturn(user);
        
        TechGalleryUser result = service.getUser(1L);
        
        assertNotNull(result);
        assertEquals(Long.valueOf(1L), result.getId());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test(expected = NotFoundException.class)
    public void testGetUser_NotFound() throws Exception {
        when(mockUserDao.findById(1L)).thenReturn(null);
        
        service.getUser(1L);
    }

    @Test
    public void testAddUser_Success() throws Exception {
        TechGalleryUser user = createTestUser("test@example.com", "123");
        
        TechGalleryUser result = service.addUser(user);
        
        assertNotNull(result);
        verify(mockUserDao).add(user);
    }

    @Test(expected = BadRequestException.class)
    public void testAddUser_InvalidData() throws Exception {
        TechGalleryUser user = createTestUser("", "123");
        
        service.addUser(user);
    }

    @Test
    public void testGetUserByLogin_Success() throws Exception {
        TechGalleryUser user = createTestUser("test@example.com", "123");
        when(mockUserDao.findByLogin("test@example.com")).thenReturn(user);
        
        TechGalleryUser result = service.getUserByLogin("test@example.com");
        
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test(expected = NotFoundException.class)
    public void testGetUserByLogin_NotFound() throws Exception {
        when(mockUserDao.findByLogin("nonexistent@example.com")).thenReturn(null);
        
        service.getUserByLogin("nonexistent@example.com");
    }

    @Test
    public void testGetUserByEmail_Success() throws Exception {
        TechGalleryUser user = createTestUser("test@example.com", "123");
        when(mockUserDao.findByEmail("test@example.com")).thenReturn(user);
        
        TechGalleryUser result = service.getUserByEmail("test@example.com");
        
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test(expected = NotFoundException.class)
    public void testGetUserByEmail_NotFound() throws Exception {
        when(mockUserDao.findByEmail("nonexistent@example.com")).thenReturn(null);
        
        service.getUserByEmail("nonexistent@example.com");
    }

    @Test
    public void testGetUserByGoogleId_Success() throws Exception {
        TechGalleryUser user = createTestUser("test@example.com", "123");
        when(mockUserDao.findByGoogleId("123")).thenReturn(user);
        
        TechGalleryUser result = service.getUserByGoogleId("123");
        
        assertNotNull(result);
        assertEquals("123", result.getGoogleId());
    }

    @Test
    public void testGetUserByGoogleId_NotFound() throws Exception {
        when(mockUserDao.findByGoogleId("nonexistent")).thenReturn(null);
        
        TechGalleryUser result = service.getUserByGoogleId("nonexistent");
        
        assertNull(result);
    }

    @Test
    public void testUpdateUser_Success() throws Exception {
        TechGalleryUser user = createTestUser("test@example.com", "123");
        user.setId(1L);
        
        TechGalleryUser result = service.updateUser(user);
        
        assertNotNull(result);
        verify(mockUserDao).update(user);
    }

    @Test(expected = BadRequestException.class)
    public void testUpdateUser_InvalidData() throws Exception {
        TechGalleryUser user = createTestUser("", "123");
        user.setId(1L);
        
        service.updateUser(user);
    }

    @Test
    public void testUserDataIsValid_ValidUser() throws Exception {
        TechGalleryUser user = createTestUser("test@example.com", "123");
        
        Method method = UserServiceTGImpl.class.getDeclaredMethod("userDataIsValid", TechGalleryUser.class);
        method.setAccessible(true);
        
        boolean result = (Boolean) method.invoke(null, user);
        
        assertTrue(result);
    }

    @Test
    public void testUserDataIsValid_NullUser() throws Exception {
        Method method = UserServiceTGImpl.class.getDeclaredMethod("userDataIsValid", TechGalleryUser.class);
        method.setAccessible(true);
        
        boolean result = (Boolean) method.invoke(null, (TechGalleryUser) null);
        
        assertFalse(result);
    }

    @Test
    public void testUserDataIsValid_BlankName() throws Exception {
        TechGalleryUser user = createTestUser("test@example.com", "123");
        user.setName("");
        
        Method method = UserServiceTGImpl.class.getDeclaredMethod("userDataIsValid", TechGalleryUser.class);
        method.setAccessible(true);
        
        boolean result = (Boolean) method.invoke(null, user);
        
        assertFalse(result);
    }

    @Test
    public void testUserDataIsValid_BlankEmail() throws Exception {
        TechGalleryUser user = createTestUser("", "123");
        
        Method method = UserServiceTGImpl.class.getDeclaredMethod("userDataIsValid", TechGalleryUser.class);
        method.setAccessible(true);
        
        boolean result = (Boolean) method.invoke(null, user);
        
        assertFalse(result);
    }

    @Test
    public void testValidateUser_Success() throws Exception {
        when(mockUser.getUserId()).thenReturn("123");
        TechGalleryUser user = createTestUser("test@example.com", "123");
        when(mockUserDao.findByGoogleId("123")).thenReturn(user);
        
        TechGalleryUser result = service.validateUser(mockUser);
        
        assertNotNull(result);
        assertEquals("123", result.getGoogleId());
    }

    @Test(expected = BadRequestException.class)
    public void testValidateUser_NullUser() throws Exception {
        service.validateUser(null);
    }

    @Test(expected = BadRequestException.class)
    public void testValidateUser_NullUserId() throws Exception {
        when(mockUser.getUserId()).thenReturn(null);
        
        service.validateUser(mockUser);
    }

    @Test(expected = BadRequestException.class)
    public void testValidateUser_EmptyUserId() throws Exception {
        when(mockUser.getUserId()).thenReturn("");
        
        service.validateUser(mockUser);
    }

    @Test(expected = NotFoundException.class)
    public void testValidateUser_UserNotExists() throws Exception {
        when(mockUser.getUserId()).thenReturn("123");
        when(mockUserDao.findByGoogleId("123")).thenReturn(null);
        
        service.validateUser(mockUser);
    }
}
