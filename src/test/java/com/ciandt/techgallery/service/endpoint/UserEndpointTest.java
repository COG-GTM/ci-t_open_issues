package com.ciandt.techgallery.service.endpoint;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.users.User;

import com.ciandt.techgallery.persistence.model.TechGalleryUser;
import com.ciandt.techgallery.service.BaseServiceTest;
import com.ciandt.techgallery.service.UserServiceTG;
import com.ciandt.techgallery.service.model.UserResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class UserEndpointTest extends BaseServiceTest {

    @Mock
    private UserServiceTG mockUserService;
    
    @Mock
    private User mockUser;
    
    @Mock
    private HttpServletRequest mockRequest;
    
    private UserEndpoint endpoint;
    
    @Before
    public void setUp() throws Exception {
        super.setUpBase();
        MockitoAnnotations.initMocks(this);
        endpoint = new UserEndpoint();
        
        Field serviceField = UserEndpoint.class.getDeclaredField("service");
        serviceField.setAccessible(true);
        serviceField.set(endpoint, mockUserService);
    }

    @Test
    public void testHandleLogin_Success() throws Exception {
        TechGalleryUser user = createTestUser("test@example.com", "123");
        
        when(mockUserService.handleLogin(120, mockUser, mockRequest)).thenReturn(user);
        
        TechGalleryUser result = endpoint.handleLogin(120, mockUser, mockRequest);
        
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(mockUserService).handleLogin(120, mockUser, mockRequest);
    }

    @Test(expected = BadRequestException.class)
    public void testHandleLogin_BadRequest() throws Exception {
        when(mockUserService.handleLogin(120, mockUser, mockRequest))
            .thenThrow(new BadRequestException("Invalid user"));
        
        endpoint.handleLogin(120, mockUser, mockRequest);
    }

    @Test(expected = NotFoundException.class)
    public void testHandleLogin_NotFound() throws Exception {
        when(mockUserService.handleLogin(120, mockUser, mockRequest))
            .thenThrow(new NotFoundException("User not found"));
        
        endpoint.handleLogin(120, mockUser, mockRequest);
    }

    @Test
    public void testUsersAutoComplete_Success() throws Exception {
        UserResponse user1 = new UserResponse();
        user1.setEmail("test1@example.com");
        user1.setName("Test User 1");
        
        UserResponse user2 = new UserResponse();
        user2.setEmail("test2@example.com");
        user2.setName("Test User 2");
        
        List<UserResponse> users = Arrays.asList(user1, user2);
        
        when(mockUserService.getUsersByPartialLogin("test")).thenReturn(users);
        
        List<UserResponse> result = endpoint.usersAutoComplete("test");
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("test1@example.com", result.get(0).getEmail());
        verify(mockUserService).getUsersByPartialLogin("test");
    }

    @Test(expected = NotFoundException.class)
    public void testUsersAutoComplete_NotFound() throws Exception {
        when(mockUserService.getUsersByPartialLogin("nonexistent"))
            .thenThrow(new NotFoundException("No users found"));
        
        endpoint.usersAutoComplete("nonexistent");
    }

    @Test(expected = BadRequestException.class)
    public void testUsersAutoComplete_BadRequest() throws Exception {
        when(mockUserService.getUsersByPartialLogin(""))
            .thenThrow(new BadRequestException("Query cannot be empty"));
        
        endpoint.usersAutoComplete("");
    }

    @Test
    public void testGetLoggedUser_Success() throws Exception {
        TechGalleryUser user = createTestUser("test@example.com", "123");
        
        when(mockUser.getEmail()).thenReturn("test@example.com");
        when(mockUserService.getUserByEmail("test@example.com")).thenReturn(user);
        
        TechGalleryUser result = endpoint.getLoggedUser(mockUser);
        
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(mockUserService).getUserByEmail("test@example.com");
    }

    @Test(expected = NotFoundException.class)
    public void testGetLoggedUser_NotFound() throws Exception {
        when(mockUser.getEmail()).thenReturn("nonexistent@example.com");
        when(mockUserService.getUserByEmail("nonexistent@example.com"))
            .thenThrow(new NotFoundException("User not found"));
        
        endpoint.getLoggedUser(mockUser);
    }

    @Test
    public void testSaveUserPreference_Success() throws Exception {
        TechGalleryUser user = createTestUser("test@example.com", "123");
        user.setPostGooglePlusPreference(true);
        
        when(mockUserService.saveUserPreference(true, mockUser)).thenReturn(user);
        
        TechGalleryUser result = endpoint.saveUserPreference(true, mockUser);
        
        assertNotNull(result);
        assertTrue(result.getPostGooglePlusPreference());
        verify(mockUserService).saveUserPreference(true, mockUser);
    }

    @Test(expected = BadRequestException.class)
    public void testSaveUserPreference_BadRequest() throws Exception {
        when(mockUserService.saveUserPreference(true, mockUser))
            .thenThrow(new BadRequestException("Invalid preference"));
        
        endpoint.saveUserPreference(true, mockUser);
    }

    @Test(expected = NotFoundException.class)
    public void testSaveUserPreference_NotFound() throws Exception {
        when(mockUserService.saveUserPreference(true, mockUser))
            .thenThrow(new NotFoundException("User not found"));
        
        endpoint.saveUserPreference(true, mockUser);
    }
}
