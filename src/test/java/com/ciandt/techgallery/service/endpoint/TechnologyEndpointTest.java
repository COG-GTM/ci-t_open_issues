package com.ciandt.techgallery.service.endpoint;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.users.User;

import com.ciandt.techgallery.persistence.model.TechGalleryUser;
import com.ciandt.techgallery.persistence.model.Technology;
import com.ciandt.techgallery.service.BaseServiceTest;
import com.ciandt.techgallery.service.TechnologyFollowersService;
import com.ciandt.techgallery.service.TechnologyService;
import com.ciandt.techgallery.service.UserServiceTG;
import com.ciandt.techgallery.service.model.Response;
import com.ciandt.techgallery.service.model.TechnologiesResponse;
import com.ciandt.techgallery.service.model.TechnologyFilter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class TechnologyEndpointTest extends BaseServiceTest {

    @Mock
    private TechnologyService mockTechnologyService;
    
    @Mock
    private TechnologyFollowersService mockFollowersService;
    
    @Mock
    private UserServiceTG mockUserService;
    
    @Mock
    private User mockUser;
    
    private TechnologyEndpoint endpoint;
    
    @Before
    public void setUp() throws Exception {
        super.setUpBase();
        MockitoAnnotations.initMocks(this);
        endpoint = new TechnologyEndpoint();
        
        Field serviceField = TechnologyEndpoint.class.getDeclaredField("service");
        serviceField.setAccessible(true);
        serviceField.set(endpoint, mockTechnologyService);
        
        Field followersServiceField = TechnologyEndpoint.class.getDeclaredField("followersService");
        followersServiceField.setAccessible(true);
        followersServiceField.set(endpoint, mockFollowersService);
        
        Field userServiceField = TechnologyEndpoint.class.getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(endpoint, mockUserService);
    }

    @Test
    public void testAddOrUpdateTechnology_Success() throws Exception {
        Technology inputTech = createTestTechnology("java", "Java");
        Technology resultTech = createTestTechnology("java", "Java");
        
        when(mockTechnologyService.addOrUpdateTechnology(inputTech, mockUser)).thenReturn(resultTech);
        
        Technology result = endpoint.addOrUpdateTechnology(inputTech, mockUser);
        
        assertNotNull(result);
        assertEquals("java", result.getId());
        verify(mockTechnologyService).addOrUpdateTechnology(inputTech, mockUser);
    }

    @Test(expected = BadRequestException.class)
    public void testAddOrUpdateTechnology_BadRequest() throws Exception {
        Technology inputTech = createTestTechnology("", "");
        
        when(mockTechnologyService.addOrUpdateTechnology(inputTech, mockUser))
            .thenThrow(new BadRequestException("Invalid technology"));
        
        endpoint.addOrUpdateTechnology(inputTech, mockUser);
    }

    @Test
    public void testGetTechnologies_Success() throws Exception {
        TechnologiesResponse response = new TechnologiesResponse();
        
        when(mockTechnologyService.getTechnologies(mockUser)).thenReturn(response);
        
        Response result = endpoint.getTechnologies(mockUser);
        
        assertNotNull(result);
        assertTrue(result instanceof TechnologiesResponse);
        verify(mockTechnologyService).getTechnologies(mockUser);
    }

    @Test(expected = NotFoundException.class)
    public void testGetTechnologies_NotFound() throws Exception {
        when(mockTechnologyService.getTechnologies(mockUser))
            .thenThrow(new NotFoundException("No technologies found"));
        
        endpoint.getTechnologies(mockUser);
    }

    @Test
    public void testGetTechnology_Success() throws Exception {
        Technology tech = createTestTechnology("java", "Java");
        
        when(mockTechnologyService.getTechnologyById("java", mockUser)).thenReturn(tech);
        
        Technology result = endpoint.getTechnology("java", mockUser);
        
        assertNotNull(result);
        assertEquals("java", result.getId());
        verify(mockTechnologyService).getTechnologyById("java", mockUser);
    }

    @Test(expected = NotFoundException.class)
    public void testGetTechnology_NotFound() throws Exception {
        when(mockTechnologyService.getTechnologyById("nonexistent", mockUser))
            .thenThrow(new NotFoundException("Technology not found"));
        
        endpoint.getTechnology("nonexistent", mockUser);
    }

    @Test
    public void testFindTechnologyByFilter_Success() throws Exception {
        TechnologiesResponse response = new TechnologiesResponse();
        
        when(mockTechnologyService.findTechnologiesByFilter(any(TechnologyFilter.class), eq(mockUser))).thenReturn(response);
        
        Response result = endpoint.findTechnologyByFilter(mockUser, "java", null, null, null, null);
        
        assertNotNull(result);
        assertTrue(result instanceof TechnologiesResponse);
        verify(mockTechnologyService).findTechnologiesByFilter(any(TechnologyFilter.class), eq(mockUser));
    }

    @Test
    public void testGetOrderOptions_Success() throws Exception {
        List<String> options = Arrays.asList("name", "date", "popularity");
        
        when(mockTechnologyService.getOrderOptions(mockUser)).thenReturn(options);
        
        List<String> result = endpoint.getOrderOptions(mockUser);
        
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains("name"));
        verify(mockTechnologyService).getOrderOptions(mockUser);
    }

    @Test
    public void testFollowTechnology_Success() throws Exception {
        Technology tech = createTestTechnology("java", "Java");
        TechGalleryUser user = createTestUser("test@example.com", "123");
        
        when(mockUser.getUserId()).thenReturn("123");
        when(mockUserService.getUserByGoogleId("123")).thenReturn(user);
        when(mockFollowersService.followTechnology("java", user)).thenReturn(tech);
        
        Technology result = endpoint.followTechnology("java", mockUser);
        
        assertNotNull(result);
        assertEquals("java", result.getId());
        verify(mockUserService).getUserByGoogleId("123");
        verify(mockFollowersService).followTechnology("java", user);
    }

    @Test(expected = BadRequestException.class)
    public void testFollowTechnology_BadRequest() throws Exception {
        when(mockUser.getUserId()).thenReturn("123");
        when(mockUserService.getUserByGoogleId("123")).thenReturn(null);
        when(mockFollowersService.followTechnology("java", null))
            .thenThrow(new BadRequestException("User not found"));
        
        endpoint.followTechnology("java", mockUser);
    }

    @Test
    public void testDeleteTechnology_Success() throws Exception {
        Technology tech = createTestTechnology("java", "Java");
        tech.setActive(false);
        
        when(mockTechnologyService.deleteTechnology("java", mockUser)).thenReturn(tech);
        
        Technology result = endpoint.deleteTechnology("java", mockUser);
        
        assertNotNull(result);
        assertEquals("java", result.getId());
        assertFalse(result.getActive());
        verify(mockTechnologyService).deleteTechnology("java", mockUser);
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteTechnology_NotFound() throws Exception {
        when(mockTechnologyService.deleteTechnology("nonexistent", mockUser))
            .thenThrow(new NotFoundException("Technology not found"));
        
        endpoint.deleteTechnology("nonexistent", mockUser);
    }
}
