# KMMtg Improvement Tasks

This document contains a prioritized list of actionable improvement tasks for the KMMtg project. Each task is designed to enhance the project's architecture, code quality, or functionality.

## Architecture Improvements

1. [ ] Complete Android support in all modules
   - Fix Android configuration in scryfall module
   - Uncomment and fix Android-specific code in build.gradle.kts files
   - Implement Android-specific database drivers

2. [ ] Implement proper multiplatform support for collection-import module
   - Research alternatives to kotlin-csv that support iOS
   - Implement iOS-specific file handling

3. [ ] Implement a proper dependency injection framework across all modules
   - Standardize Koin usage across all modules
   - Create module definitions for each module

4. [ ] Implement a comprehensive error handling strategy
   - Standardize error types and handling across modules
   - Improve error messages and logging

5. [ ] Implement a proper user authentication and authorization system
   - Complete User table implementation
   - Add password/authentication fields
   - Implement token-based authentication

## Database Improvements

6. [ ] Enhance Card schema with additional attributes
   - Add mana cost, card type, rarity, and other important MTG card attributes
   - Update queries to support filtering by these attributes

7. [ ] Complete User-Deck relationship implementation
   - Uncomment and implement user_id foreign key in Deck table
   - Implement user-specific deck queries

8. [ ] Add query to update card quantity in a deck
   - Implement updateCardQuantityInDeck query in DeckCard.sq

9. [ ] Implement database migrations strategy
   - Add versioning to database schema
   - Create migration scripts for future schema changes

## API Improvements

10. [ ] Expand Scryfall API coverage
    - Implement additional endpoints (card by ID, random card, etc.)
    - Add support for advanced search parameters

11. [ ] Implement pagination handling for Scryfall API responses
    - Add support for retrieving multiple pages of results
    - Implement a paging mechanism for large result sets

12. [ ] Add caching for Scryfall API responses
    - Implement a caching strategy to reduce API calls
    - Add TTL (time-to-live) for cached responses

## Testing and Documentation

13. [ ] Implement comprehensive unit tests for all modules
    - Add tests for database operations
    - Add tests for API client
    - Add tests for collection import/export

14. [ ] Implement integration tests
    - Add tests for interactions between modules
    - Add end-to-end tests for key user flows

15. [ ] Add KDoc documentation to all public APIs
    - Document all public classes, interfaces, and functions
    - Include examples where appropriate

16. [ ] Create user documentation
    - Add usage examples for CLI
    - Document supported file formats for collection import/export

## Code Quality Improvements

17. [ ] Replace non-null assertions (!!) with proper null handling
    - Use Elvis operator (?:) or other null-safe approaches
    - Add validation to prevent null pointer exceptions

18. [ ] Implement proper logging throughout the application
    - Standardize logging approach
    - Add appropriate log levels for different types of messages

19. [ ] Add input validation for all user inputs
    - Validate CSV file formats
    - Validate deck construction rules

20. [ ] Implement proper exception handling
    - Replace generic error messages with specific ones
    - Add context to error messages

## Feature Improvements

21. [ ] Add support for additional collection import/export formats
    - Support more popular MTG collection management tools
    - Implement a plugin system for custom formats

22. [ ] Implement deck validation against format rules
    - Add support for checking deck legality in different formats
    - Implement card legality checking

23. [ ] Add support for card pricing information
    - Integrate with pricing APIs
    - Add price tracking for collections

24. [ ] Enhance CLI with more interactive features
    - Implement autocomplete for card names
    - Add visual card representation in terminal