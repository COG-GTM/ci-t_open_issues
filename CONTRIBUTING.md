# Contributing to Tech Gallery

Thank you for your interest in contributing to Tech Gallery! This document provides
guidelines and information for contributors.

## Development Setup

### Prerequisites

- Java JDK 1.7 or higher
- Maven 3.1.0 or higher
- Node.js v0.10.25
- npm 1.3.10
- Google App Engine SDK

### Getting Started

1. **Fork and Clone**

   ```bash
   git clone https://github.com/COG-GTM/ci-t_open_issues.git
   cd ci-t_open_issues
   ```

2. **Install Dependencies**

   ```bash
   mvn clean install
   ```

3. **Run Development Server**

   ```bash
   mvn appengine:devserver
   ```

4. **Verify Setup**
   - Application: <http://localhost:8080/>
   - API Explorer: <http://localhost:8080/_ah/api/explorer>

## Development Workflow

### Branch Naming

- Feature branches: `feature/short-description`
- Bug fixes: `bugfix/issue-description`
- Documentation: `docs/update-description`

### Making Changes

1. **Create Feature Branch**

   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Make Your Changes**
   - Follow existing code patterns and conventions
   - Add tests for new functionality
   - Update documentation as needed

3. **Test Your Changes**

   ```bash
   # Run unit tests
   mvn test
   
   # Run quality checks
   mvn checkstyle:check
   
   # Test frontend changes
   cd src/main/webapp
   gulp jshint
   gulp csslint
   ```

4. **Commit Changes**

   ```bash
   git add <changed-files>
   git commit -m "feat: add new technology filtering feature"
   ```

## Coding Standards

### Java Code

- Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- Use meaningful variable and method names
- Add Javadoc comments for public methods
- Maximum line length: 100 characters

### JavaScript Code

- Use JSDoc comments for functions
- Follow existing AngularJS patterns
- Use meaningful variable names
- Avoid global variables

### Code Quality Checks

```bash
# Run all quality checks
mvn clean install -P QA

# Individual checks
mvn checkstyle:check          # Java style
mvn cobertura:cobertura       # Test coverage
mvn dependency-check:check    # Security vulnerabilities
```

## Testing Guidelines

### Unit Tests

- Write tests for all new service methods
- Use JUnit 4.12 and Mockito for testing
- Maintain test coverage above 80%
- Test both success and error scenarios

### Integration Tests

- Test API endpoints with authentication
- Verify database operations
- Test email functionality (use test queues)

### Frontend Testing

- Test AngularJS controllers and services
- Verify UI interactions
- Test authentication flows

## Pull Request Process

### Before Submitting

1. **Ensure All Tests Pass**

   ```bash
   mvn clean test
   ```

2. **Run Quality Checks**

   ```bash
   mvn checkstyle:check
   ```

3. **Update Documentation**
   - Update README.md if needed
   - Add/update API documentation
   - Update CHANGELOG.md

### Pull Request Template

```markdown
## Description
Brief description of changes made.

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Documentation update
- [ ] Refactoring

## Testing
- [ ] Unit tests pass
- [ ] Integration tests pass
- [ ] Manual testing completed

## Checklist
- [ ] Code follows style guidelines
- [ ] Self-review completed
- [ ] Documentation updated
- [ ] Tests added/updated
```

### Review Process

1. Automated checks must pass (CI/CD)
2. Code review by at least one maintainer
3. Manual testing if UI changes
4. Documentation review if applicable

## API Development

### Adding New Endpoints

1. Create endpoint class in `service/endpoint/`
2. Implement service logic in `service/impl/`
3. Add DAO methods if needed in `persistence/dao/`
4. Update entity models if required
5. Add comprehensive tests

### Endpoint Guidelines

- Use proper HTTP methods (GET, POST, PUT, DELETE)
- Include proper error handling
- Add authentication where required
- Document parameters and responses
- Follow existing naming conventions

## Database Changes

### Entity Modifications

1. Update entity classes in `persistence/model/`
2. Update DAO classes if needed
3. Consider migration strategy for existing data
4. Update composite indexes in `datastore-indexes.xml`

### Index Management

```xml
<!-- Example composite index -->
<datastore-index kind="Technology" ancestor="false">
    <property name="recommendation" direction="asc"/>
    <property name="lastActivity" direction="desc"/>
</datastore-index>
```

## Frontend Development

### AngularJS Guidelines

- Use existing module structure
- Follow controller naming conventions
- Use services for API communication
- Implement proper error handling

### UI/UX Guidelines

- Follow Bootstrap conventions
- Maintain responsive design
- Use existing CSS classes when possible
- Test on multiple screen sizes

## Security Guidelines

### Authentication

- All API endpoints require authentication
- Use Google OAuth with domain restriction
- Validate user permissions for admin operations
- Never expose sensitive data in client-side code

### Data Protection

- Sanitize all user inputs
- Use parameterized queries
- Validate file uploads
- Implement proper access controls

## Documentation

### Code Documentation

- Add Javadoc for all public methods
- Use JSDoc for JavaScript functions
- Include parameter descriptions and return values
- Document complex business logic

### API Documentation

- Document all endpoint parameters
- Include example requests and responses
- Document error codes and messages
- Keep documentation up to date

## Getting Help

### Resources

- [Google App Engine Documentation](https://cloud.google.com/appengine/docs)
- [AngularJS Documentation](https://docs.angularjs.org/)
- [Maven Documentation](https://maven.apache.org/guides/)

### Contact

- **Email**: <google-project@ciandt.com>
- **Issues**: Use GitHub Issues for bug reports and feature requests
- **Discussions**: Use GitHub Discussions for questions and ideas

## Code of Conduct

### Our Standards

- Be respectful and inclusive
- Focus on constructive feedback
- Help others learn and grow
- Maintain professional communication

### Reporting Issues

Report any violations to <google-project@ciandt.com>

## Release Process

### Version Numbering

- Follow semantic versioning (MAJOR.MINOR.PATCH)
- Update version in `pom.xml`
- Tag releases in Git

### Deployment

1. Create release branch
2. Update version numbers
3. Run full test suite
4. Deploy to staging environment
5. Perform acceptance testing
6. Deploy to production
7. Tag release and update documentation

Thank you for contributing to Tech Gallery!
