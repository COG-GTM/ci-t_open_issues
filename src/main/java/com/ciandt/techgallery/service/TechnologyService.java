package com.ciandt.techgallery.service;

import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;

import com.ciandt.techgallery.persistence.model.Technology;
import com.ciandt.techgallery.service.model.Response;
import com.ciandt.techgallery.service.model.TechnologyFilter;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * Services for Technologies.
 *
 * @author felipers
 *
 */
public interface TechnologyService {

  /**
   * Service for adding a technology.
   *
   * @param technology json with technology info.
   * @param user that is adding a technology.
   *
   * @return technology info or message error.
   *
   * @throws InternalServerErrorException in case some internal server error occur.
   * @throws BadRequestException in case a request with problem were made.
   */
  public Technology addOrUpdateTechnology(Technology technology, User user)
      throws BadRequestException, IOException, GeneralSecurityException;

  /**
   * Service for getting all technologies.
   *
   * @return technologies info or message error.
   * @throws InternalServerErrorException in case something goes wrong
   * @throws NotFoundException in case the information are not founded
   * @throws BadRequestException in case a request with problem were made.
   */
  Response getTechnologies(User user)
      throws InternalServerErrorException, NotFoundException, BadRequestException;

  /**
   * Service for getting all technologies according a filter.
   *
   * @param filter entity filter.
   * @return technologies info or message error.
   * @throws InternalServerErrorException in case something goes wrong
   * @throws NotFoundException in case the information are not founded
   * @throws BadRequestException in case a request with problem were made.
   */
  Response findTechnologiesByFilter(final TechnologyFilter techFilter, User user)
      throws InternalServerErrorException, NotFoundException, BadRequestException;

  /**
   * Service for getting a technology
   *
   * @param id entity id
   * @return .
   * @throws InternalServerErrorException in case something goes wrong
   * @throws NotFoundException in case the information are not founded
   * @throws BadRequestException in case a request with problem were made.
   */
  Technology getTechnologyById(String id, User user)
      throws NotFoundException, BadRequestException, InternalServerErrorException;

  List<String> getOrderOptions(User user);

  void addCommentariesCounter(Technology technology);

  void removeCommentariesCounter(Technology technology);

  void addRecomendationCounter(Technology technology, Boolean score);

  void removeRecomendationCounter(Technology technology, Boolean score);

  void updateEdorsedsCounter(Technology technology, Integer size);

  /**
   * Method to update the audit of the technology informed.
   *
   * @author <a href="mailto:joaom@ciandt.com"> João Felipe de Medeiros Moreira </a>
   * @since 22/10/2015
   *
   * @param technologyId to be updated.
   * @param user to fill the update user.
   *
   * @throws InternalServerErrorException in case something goes wrong
   * @throws NotFoundException in case the information are not founded
   * @throws BadRequestException in case a request with problem were made.
   */
  void audit(String technologyId, User user)
      throws NotFoundException, BadRequestException, InternalServerErrorException;

  /**
   * Service to delete a technology.
   *
   * @param technology entity id.
   * @return Technology or message error.
   * @throws InternalServerErrorException .
   * @throws BadRequestException .
   */
  Technology deleteTechnology(final String technologyId, User user)
      throws InternalServerErrorException, BadRequestException, NotFoundException,
      OAuthRequestException;

  /**
   * Get full technology hierarchy starting from root.
   *
   * @param rootId the root technology ID.
   * @param user the authenticated user.
   * @return Response containing the technology hierarchy.
   * @throws NotFoundException in case the information are not founded
   * @throws BadRequestException in case a request with problem were made.
   * @throws InternalServerErrorException in case something goes wrong
   */
  Response getTechnologyHierarchy(String rootId, User user)
      throws NotFoundException, BadRequestException, InternalServerErrorException;

  /**
   * Get direct child technologies.
   *
   * @param parentId the parent technology ID.
   * @param user the authenticated user.
   * @return list of child technologies.
   * @throws NotFoundException in case the information are not founded
   * @throws BadRequestException in case a request with problem were made.
   * @throws InternalServerErrorException in case something goes wrong
   */
  List<Technology> getChildTechnologies(String parentId, User user)
      throws NotFoundException, BadRequestException, InternalServerErrorException;

  /**
   * Set parent-child relationship between technologies.
   *
   * @param childId the child technology ID.
   * @param parentId the parent technology ID.
   * @param user the authenticated user.
   * @return the updated child technology.
   * @throws NotFoundException in case the information are not founded
   * @throws BadRequestException in case a request with problem were made.
   * @throws InternalServerErrorException in case something goes wrong
   */
  Technology setParentTechnology(String childId, String parentId, User user)
      throws NotFoundException, BadRequestException, InternalServerErrorException;

  /**
   * Validate technology hierarchy to prevent circular references.
   *
   * @param tech the technology to validate.
   * @throws BadRequestException if circular reference is detected.
   */
  void validateHierarchy(Technology tech) throws BadRequestException;
}
