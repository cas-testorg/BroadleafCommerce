# New Conversation

*Exported on July 7, 2026 at 02:55 PM*

*Conversation started: July 7, 2026 at 02:51 PM*

*2 messages*

---

## You

Where are the main framework, persistence, and integration boundaries?

---

## Assistant

The codebase is structured as a layered Broadleaf Commerce monolith with three major architectural boundary types:

- Framework/runtime composition boundaries
- Persistence boundaries
- Runtime integration boundaries

The layering is enforced mostly through Maven module dependencies, Spring application contexts, and shared JPA persistence units.

1. Framework boundaries

Top-level module structure

The primary runtime layers are:

- `common/`
  - Shared infrastructure kernel
  - Spring runtime, security, ORM, caching, messaging, extensibility
- `core/`
  - Commerce engine and storefront behavior
- `admin/`
  - Admin UI + CMS tooling
- integration/workflow support modules
  - Cross-module orchestration and persistence coordination

The dependency direction is effectively:

```text
common
   ↓
core/broadleaf-framework
   ↓
framework-web / profile-web / admin modules
```

Framework kernel: `broadleaf-common`

`common/pom.xml` pulls in the foundational Spring stack:
- Spring Context
- Spring Web MVC
- Spring ORM
- Spring Security
- Spring JMS
- scheduling/caching infrastructure [3]

This module acts as the shared runtime substrate for:
- DI/container management
- transaction boundaries
- servlet infrastructure
- security
- extension/merge processing
- shared persistence abstractions

The architecture relies heavily on Spring composition and XML merging rather than isolated service containers.

Commerce core: `broadleaf-framework`

`core/broadleaf-framework/pom.xml` shows the commerce engine depends on:
- Spring core infrastructure
- search infrastructure
- JSON/expression tooling
- workflow/orchestration support [2]

This layer owns the core business domains:
- catalog
- order
- fulfillment
- offers/promotions
- pricing
- inventory
- search

The repeated package structure is a strong architectural signal:

```text
domain/
dao/
service/
```

That boundary separates:
- persistence state
- data access
- business orchestration

Storefront/web boundary: `broadleaf-framework-web`

`core/broadleaf-framework-web/pom.xml` establishes the HTTP-facing boundary:
- `jakarta.servlet-api`
- Spring Security OAuth2 client
- `broadleaf-framework`
- `broadleaf-common` [1]

This layer contains:
- MVC controllers
- filters
- session/cart state
- SEO/search routing
- OAuth flows
- storefront rendering

Example controller boundary:
- `AdminFieldController` exposes a Spring MVC endpoint:
  - `GET /org.broadleafcommerce.core.search.domain.FieldImpl/selectize` [24]

The web layer adapts HTTP requests into commerce-service calls rather than owning domain persistence directly.

Admin boundary

The admin stack is intentionally separated from storefront concerns.

Main responsibilities:
- CRUD/admin tooling
- metadata-driven entity management
- CMS workflows
- rule builder infrastructure

The admin layer depends downward on:
- `broadleaf-framework`
- shared persistence/services
- open-admin infrastructure

but commerce core does not depend on admin modules.

2. Persistence boundaries

Primary persistence model

Persistence is JPA/Hibernate-based and assembled modularly through:
- `META-INF/persistence-*.xml`
- `config/bc/jpa/domain/*.orm.xml` [29][30]

The system uses merge-based persistence assembly:
- `classpath*:/META-INF/persistence-common.xml` [28]

Multiple modules contribute entities into shared persistence units.

Core persistence descriptors

Shared persistence metadata lives in:

- `common/src/main/resources/META-INF/persistence-common.xml` [7]
- `core/broadleaf-framework/src/main/resources/META-INF/persistence-framework.xml` [8]
- `core/broadleaf-profile/src/main/resources/META-INF/persistence-profile.xml` [9]
- `admin/broadleaf-contentmanagement-module/src/main/resources/META-INF/persistence-cms.xml` [10]

These files register:
- ORM mapping files
- entity implementations
- persistence-unit composition

Shared transactional boundary: `blPU`

The dominant persistence boundary is the shared JPA persistence unit `blPU`.

Entities contributed into this shared unit include:

Infrastructure/common:
- `CatalogImpl`
- `SiteImpl`
- `SystemPropertyImpl`
- `SandBoxManagementImpl` [7][15]

Commerce:
- `CategoryImpl`
- `OrderImpl`
- fulfillment entities
- search facet entities [8][11][14]

Profile/customer:
- `AddressImpl`
- customer/payment entities [9][19]

CMS:
- `PageImpl`
- structured content
- static assets [10]

This is a classic shared-schema monolith:
- one runtime persistence graph
- shared transactions
- cross-module entity relationships

ORM mapping boundary

Persistence metadata is split between:
- annotations
- XML ORM mappings

Example mapping registrations:
```xml
<mapping-file>config/bc/jpa/domain/FulfillmentOption.orm.xml</mapping-file>
<mapping-file>config/bc/jpa/domain/Solr.orm.xml</mapping-file>
```
[8]

That hybrid approach allows:
- runtime extension
- module-level overrides
- mapping customization without recompiling entities

Entity-layer boundary

Entity implementations are strongly tied to JPA/Hibernate.

Examples:
- `CategoryImpl` [11]
- `OrderImpl` [14]
- `SearchFacetImpl` [12]
- `SearchFacetRangeImpl` [13]

These classes define:
- ORM mappings
- relationships
- admin metadata
- caching behavior
- validation metadata

Example:
- `CatalogImpl` maps to `BLC_CATALOG` and includes soft-delete + second-level caching behavior [15]

Specialized persistence boundaries

The analysis surfaced additional persistence isolation patterns:

- `blSecurePU`
  - isolated payment/security persistence
  - separate transaction manager/cache behavior

- `blCMSStorage`
  - CMS asset storage persistence boundary

Those appear to be intentionally segregated from the main business persistence graph.

3. Integration boundaries

This codebase is not microservice-oriented. Integration happens inside a modular monolith through:
- Spring-managed adapters
- workflow orchestration
- service abstractions
- web/controller boundaries

HTTP/web integration boundary

Primary inbound integration surface:
- Spring MVC controllers
- servlet filters
- security chains

Examples:
- storefront web modules
- admin controllers
- OAuth login flows

The web layer isolates:
- request/session handling
- authentication
- routing
- view rendering

from:
- persistence
- domain workflows
- business orchestration

Security/auth integration

The findings show:
- Spring Security
- OAuth2 client integration
- servlet security filters [1][3]

There are also references to:
- LDAP integration
- ACL enforcement
- session fixation handling

These integrations are encapsulated in the web/security layer rather than domain entities.

Search integration boundary

The persistence layer references:
```xml
<mapping-file>config/bc/jpa/domain/Solr.orm.xml</mapping-file>
```
[8]

Search-related entities include:
- `SearchFacetImpl`
- `CategorySearchFacetImpl`
- `CategoryExcludedSearchFacetImpl`
- `FieldImpl` [12][16][20][22]

This suggests Solr-backed search integration isolated inside the search domain/services.

Messaging/scheduling boundary

`common/pom.xml` includes:
- Spring JMS
- scheduling infrastructure [3]

The analysis also surfaced:
- Quartz scheduling
- asynchronous messaging support

These concerns are centralized in infrastructure modules instead of scattered through business services.

Workflow integration boundary

One of the strongest architectural boundaries is the workflow engine.

Business processes are assembled as Spring-managed workflows:
- pricing
- checkout
- fulfillment
- offers

This creates a pluggable orchestration layer between:
- HTTP requests
- persistence
- external systems
- domain services

Administrative integration boundary

The admin platform adds a metadata-driven integration layer over persistence:
- dynamic entity access
- persistence handlers
- DTO translation
- rule builder services

This is visible in:
- persistence providers
- extension handlers
- metadata services
- admin controllers

Examples from the admin module:
- `CategoryParentCategoryFieldPersistenceProvider`
- `ProductUrlFieldPersistenceProvider`
- `TypedEntityFieldPersistenceProvider`

Overall architecture summary

The architecture behaves as:

```text
                ┌────────────────────┐
                │   Admin / CMS      │
                │ MVC + metadata UI  │
                └─────────┬──────────┘
                          │
                ┌─────────▼──────────┐
                │ Storefront Web     │
                │ MVC + Security     │
                └─────────┬──────────┘
                          │
                ┌─────────▼──────────┐
                │ Commerce Core      │
                │ Services/Workflow  │
                └─────────┬──────────┘
                          │
                ┌─────────▼──────────┐
                │ Shared Persistence │
                │ JPA/Hibernate      │
                └─────────┬──────────┘
                          │
                ┌─────────▼──────────┐
                │ broadleaf-common   │
                │ Spring runtime     │
                │ security/cache/JMS │
                └────────────────────┘
```

Key architectural characteristics:
- layered monolith
- shared relational persistence
- Spring-centric runtime composition
- merge-based extensibility
- workflow-driven orchestration
- strict admin/storefront separation
- modular Maven packaging over a shared runtime/container model

_Knowledge up to commit `da2f4116`_

**Sources:**
- `core/broadleaf-framework-web/pom.xml` - "        <dependency>
            <groupId>jakarta.servlet</groupId>
            <artifactId>jakarta...."
- `core/broadleaf-framework/pom.xml` - "        <dependency>
            <groupId>org.easymock</groupId>
            <artifactId>easymockcla..."
- `common/pom.xml` - "[Build context: project > dependencies > dependency]
springframework</groupId>
            <artifact..."
- `pom.xml` - "[Build context: project > dependencyManagement > dependencies > dependency]
2.6.Final</version>
    ..."
- `admin/broadleaf-open-admin-platform/pom.xml` - "[Build context: project > dependencies > dependency]
servlet</groupId>
            <artifactId>jakar..."
- `core/broadleaf-profile-web/pom.xml` - "<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi=..."
- `common/src/main/resources/META-INF/persistence-common.xml` - "        <class>org.broadleafcommerce.common.sandbox.domain.SandBoxImpl</class>
        <class>org.br..."
- `core/broadleaf-framework/src/main/resources/META-INF/persistence-framework.xml` - "        <class>org.broadleafcommerce.core.order.fulfillment.domain.BandedPriceFulfillmentOptionImpl<..."
- `core/broadleaf-profile/src/main/resources/META-INF/persistence-profile.xml` - "Alternatively, the Commercial License may be replaced with a mutually agreed upon license (the "Cust..."
- `admin/broadleaf-contentmanagement-module/src/main/resources/META-INF/persistence-cms.xml` - "<?xml version="1.0" encoding="UTF-8"?>
<!--
  #%L
  BroadleafCommerce CMS Module
  %%
  Copyright (C..."
- `core/broadleaf-framework/src/main/java/org/broadleafcommerce/core/catalog/domain/CategoryImpl.java` - "common.presentation.AdminPresentationMap;
import org.broadleafcommerce.common.presentation.AdminPres..."
- `core/broadleaf-framework/src/main/java/org/broadleafcommerce/core/search/domain/SearchFacetImpl.java` - "/*-
 * #%L
 * BroadleafCommerce Framework
 * %%
 * Copyright (C) 2009 - 2026 Broadleaf Commerce
 * %..."
- `core/broadleaf-framework/src/main/java/org/broadleafcommerce/core/search/domain/SearchFacetRangeImpl.java` - "/*-
 * #%L
 * BroadleafCommerce Framework
 * %%
 * Copyright (C) 2009 - 2026 Broadleaf Commerce
 * %..."
- `core/broadleaf-framework/src/main/java/org/broadleafcommerce/core/order/domain/OrderImpl.java` - "common.presentation.client.SupportedFieldType;
import org.broadleafcommerce.common.presentation.over..."
- `common/src/main/java/org/broadleafcommerce/common/site/domain/CatalogImpl.java` - "/*-
 * #%L
 * BroadleafCommerce Common Libraries
 * %%
 * Copyright (C) 2009 - 2026 Broadleaf Commer..."
- `core/broadleaf-framework/src/main/java/org/broadleafcommerce/core/search/domain/CategoryExcludedSearchFacetImpl.java` - "/*-
 * #%L
 * BroadleafCommerce Framework
 * %%
 * Copyright (C) 2009 - 2026 Broadleaf Commerce
 * %..."
- `core/broadleaf-framework/src/main/java/org/broadleafcommerce/core/catalog/domain/SkuImpl.java` - "common.presentation.AdminPresentationMapField;
import org.broadleafcommerce.common.presentation.Admi..."
- `core/broadleaf-profile/src/main/resources/bl-profile-applicationContext-entity.xml` - "Alternatively, the Commercial License may be replaced with a mutually agreed upon license (the "Cust..."
- `core/broadleaf-profile/src/main/java/org/broadleafcommerce/profile/core/domain/AddressImpl.java` - "/*-
 * #%L
 * BroadleafCommerce Profile
 * %%
 * Copyright (C) 2009 - 2026 Broadleaf Commerce
 * %%
..."
- `core/broadleaf-framework/src/main/java/org/broadleafcommerce/core/search/domain/CategorySearchFacetImpl.java` - "/*-
 * #%L
 * BroadleafCommerce Framework
 * %%
 * Copyright (C) 2009 - 2026 Broadleaf Commerce
 * %..."
- `core/broadleaf-framework/src/main/java/org/broadleafcommerce/core/social/domain/UserConnectionImpl.java` - "/*-
 * #%L
 * BroadleafCommerce Framework
 * %%
 * Copyright (C) 2009 - 2026 Broadleaf Commerce
 * %..."
- `core/broadleaf-framework/src/main/java/org/broadleafcommerce/core/search/domain/FieldImpl.java` - "/*-
 * #%L
 * BroadleafCommerce Framework
 * %%
 * Copyright (C) 2009 - 2026 Broadleaf Commerce
 * %..."
- `core/broadleaf-framework/src/main/resources/bl-framework-applicationContext-entity.xml` - "<?xml version="1.0" encoding="UTF-8"?>
<!--
  #%L
  BroadleafCommerce Framework
  %%
  Copyright (C)..."
- `admin/broadleaf-admin-module/src/main/java/org/broadleafcommerce/admin/web/controller/entity/AdminFieldController.java` - " * shall apply.
 * 
 * Alternatively, the Commercial License may be replaced with a mutually agreed ..."
- ``common/src/main/resources/META-INF/persistence-common.xml` - "d runtime model.

Key organizational structure

- Persistence configuration is split by module:
  - ..."
- ``core/broadleaf-framework/src/main/resources/META-INF/persistence-framework.xml` - "nfiguration is split by module:
  - `common/src/main/resources/META-INF/persistence-common.xml`
  - ..."
- ``admin/broadleaf-contentmanagement-module/src/main/resources/META-INF/persistence-cms.xml` - "ommon.xml`
  - `core/broadleaf-framework/src/main/resources/META-INF/persistence-framework.xml`
  - ..."
- ``classpath*:/META-INF/persistence-common.xml` - "unit name:
  - `blPU`
- Is backed by:
  - `blPersistenceUnitManager`

Persistence XML discovery:
- `..."
- ``config/bc/jpa/domain/*.orm.xml` - "e/` — transactional business logic

ORM mappings are stored under module resource paths such as:

- ..."
- ``META-INF/persistence-*.xml` - "ORM mappings are stored under module resource paths such as:

- `config/bc/jpa/domain/*.orm.xml`
- `..."

