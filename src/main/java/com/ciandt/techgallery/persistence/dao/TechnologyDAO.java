package com.ciandt.techgallery.persistence.dao;

import com.ciandt.techgallery.persistence.model.Technology;

/**
 * TechnologyDAOImpl methods interface.
 *
 * @author Felipe Goncalves de Castro
 *
 */
public interface TechnologyDAO extends GenericDAO<Technology, String> {

  /**
   * Method to find technologies by name.
   *
   * @author <a href="mailto:joaom@ciandt.com"> João Felipe de Medeiros Moreira </a>
   * @since 13/10/2015
   *
   * @param name of the technology to find.
   *
   * @return the technology founded.
   */
  public Technology findByName(String name);

  /**
   * Method to get the technology by id active.
   *
   * @author <a href="mailto:joaom@ciandt.com"> João Felipe de Medeiros Moreira </a>
   * @since 28/10/2015
   *
   * @param id to find the technology.
   *
   * @return the technology if is active.
   */
  public Technology findByIdActive(String id);

  /**
   * Method to find child technologies by parent ID.
   *
   * @param parentId the parent technology ID.
   *
   * @return list of child technologies.
   */
  public List<Technology> findChildrenByParentId(String parentId);

  /**
   * Method to find root technologies (no parent).
   *
   * @return list of root technologies.
   */
  public List<Technology> findRootTechnologies();

  /**
   * Method to find technology hierarchy path from root to technology.
   *
   * @param techId the technology ID.
   *
   * @return list of technologies in hierarchy path.
   */
  public List<Technology> findTechnologyPath(String techId);
}
