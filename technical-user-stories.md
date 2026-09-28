# BroadleafCommerce technical user stories

CoreStory tech spec for BroadleafCommerce — 30 stories across 10 epics, all written from the Admin User persona.

**Source:** technical specification, retrieved 14 Sep 2026  
**CoreStory project:** BroadleafCommerce (`project_id` 603)  
**Canvas:** `technical-user-stories.canvas.tsx`

| Metric | Value |
| --- | --- |
| Technical stories | 30 |
| Epics | 10 |
| Persona | Admin User |

## What these stories cover

These are implementation stories for the metadata-driven admin platform: how catalog, order, offer, CMS, and security flows use `AdminBasicEntityController`, custom persistence handlers, and `PersistenceManagerImpl`. They describe how the current system is built, not a new feature backlog.

## Stories by domain

Count of technical user stories grouped by domain. Source: CoreStory tech spec · BroadleafCommerce.

**Technical user stories by domain**

<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 420 140" role="img" aria-label="Technical user stories by domain: Catalog 9, Security 9, Promotions 6, Orders 3, CMS 3">
  <style>
    .domain-label { font: 12px system-ui, sans-serif; fill: currentColor; }
    .domain-count { font: 12px system-ui, sans-serif; fill: currentColor; font-variant-numeric: tabular-nums; }
    .domain-bar { fill: currentColor; opacity: 0.35; }
  </style>
  <text class="domain-label" x="0" y="16">Catalog</text>
  <rect class="domain-bar" x="88" y="4" width="240" height="16" rx="2" />
  <text class="domain-count" x="336" y="16">9</text>
  <text class="domain-label" x="0" y="42">Security</text>
  <rect class="domain-bar" x="88" y="30" width="240" height="16" rx="2" />
  <text class="domain-count" x="336" y="42">9</text>
  <text class="domain-label" x="0" y="68">Promotions</text>
  <rect class="domain-bar" x="88" y="56" width="160" height="16" rx="2" />
  <text class="domain-count" x="256" y="68">6</text>
  <text class="domain-label" x="0" y="94">Orders</text>
  <rect class="domain-bar" x="88" y="82" width="80" height="16" rx="2" />
  <text class="domain-count" x="176" y="94">3</text>
  <text class="domain-label" x="0" y="120">CMS</text>
  <rect class="domain-bar" x="88" y="108" width="80" height="16" rx="2" />
  <text class="domain-count" x="176" y="120">3</text>
</svg>

| Domain | Story count |
| --- | ---: |
| Catalog | 9 |
| Security | 9 |
| Promotions | 6 |
| Orders | 3 |
| CMS | 3 |

## Epic catalog

| Epic ID | Epic | Domain | Stories | Focus |
| --- | --- | --- | ---: | --- |
| `product-mgmt` | Admin Product Management | Catalog | 3 | Server-rendered product list/detail, shared persistence saves, and related SKU/category/option data. |
| `product-persist` | Admin Product Persistence Rules | Catalog | 3 | Product-specific custom persistence, category assignment rules, and sandbox-aware catalog saves. |
| `category` | Admin Category Hierarchy Management | Catalog | 3 | Parent-child category persistence inside the shared admin pipeline, aligned with the catalog aggregate. |
| `orders` | Admin Order Management | Orders | 3 | Order list/detail navigation, order-aggregate inspection, and to-one relationship resolution. |
| `offer-config` | Admin Offer Type Configuration | Promotions | 3 | Offer list/add/duplicate routes, type-specific persistence handling, and locale/currency context. |
| `offer-unique` | Offer Code Uniqueness Validation | Promotions | 3 | Reject duplicate codes, surface failures through the save pipeline, and keep order-to-code links unambiguous. |
| `assets` | Admin Static Asset Management | CMS | 3 | CMS asset CRUD through the admin console, CMS persistence-unit routing, and site-aware request state. |
| `type-validation` | Populate Request Type Validation | Security | 3 | Pre-population boolean/numeric checks that hard-stop malformed values across all metadata-driven saves. |
| `admin-user` | Admin User Self-Protection And Password Validation | Security | 3 | Admin password rules, block self-delete/self-modification, and keep mutations in the secured persistence path. |
| `customer-pw` | Customer Password Update Validation | Security | 3 | Custom customer password validation that rejects invalid updates before they touch the customer aggregate. |

## All 30 stories

| ID | Epic | Want | Key classes |
| --- | --- | --- | --- |
| TUS-01 | Admin Product Management | Open product list and detail views | AdminBaseProductController, AdminBasicEntityController |
| TUS-02 | Admin Product Management | Save products through the shared persistence pipeline | AdminBasicEntityController, AdminEntityServiceImpl |
| TUS-03 | Admin Product Management | Edit related SKUs, categories, and options | ProductImpl, SkuImpl |
| TUS-04 | Admin Order Management | Open order list and detail views | AdminOrderController, AdminBasicEntityController |
| TUS-05 | Admin Order Management | Inspect items, fulfillment, payments, and offers | OrderImpl, OrderItemImpl |
| TUS-06 | Admin Order Management | Resolve linked to-one order records | AdminOrderController, AdminEntityService |
| TUS-07 | Populate Request Type Validation | Validate boolean and numeric field types | PopulateValueRequestValidator, PersistenceManagerImpl |
| TUS-08 | Populate Request Type Validation | Stop persistence on type-validation failure | PopulateValueRequestValidator, PersistenceManagerImpl |
| TUS-09 | Populate Request Type Validation | Apply type checks on every admin section save | AdminBasicEntityController, PersistenceManagerImpl |
| TUS-10 | Admin Static Asset Management | Create and edit CMS static assets | AdminAssetController, BroadleafAdminRequestProcessor |
| TUS-11 | Admin Static Asset Management | Route assets to the CMS persistence unit | PersistenceServiceImpl, PersistenceManagerImpl |
| TUS-12 | Admin Static Asset Management | Keep asset admin site-aware | BroadleafAdminRequestProcessor, AdminAssetController |
| TUS-13 | Offer Code Uniqueness Validation | Reject duplicate offer codes | OfferCodeCustomPersistenceHandler, PersistenceManagerImpl |
| TUS-14 | Offer Code Uniqueness Validation | Surface duplicate-code errors in the save flow | AdminBasicEntityController, PersistenceManagerImpl |
| TUS-15 | Offer Code Uniqueness Validation | Keep order-to-offer-code relationships unambiguous | OfferCodeImpl, OfferImpl |
| TUS-16 | Admin User Self-Protection And Password Validation | Enforce admin password validation | AdminUserCustomPersistenceHandler, PersistenceManagerImpl |
| TUS-17 | Admin User Self-Protection And Password Validation | Block unsafe self-modification | AdminUserCustomPersistenceHandler, BroadleafAdminRequestProcessor |
| TUS-18 | Admin User Self-Protection And Password Validation | Keep admin-user mutations in the secured pipeline | PersistenceManagerImpl, AdminUserCustomPersistenceHandler |
| TUS-19 | Admin Offer Type Configuration | Open offer list, detail, add, and duplicate flows | AdminOfferController, AdminBasicEntityController |
| TUS-20 | Admin Offer Type Configuration | Apply type-specific offer persistence behavior | OfferCustomPersistenceHandler, PersistenceManagerImpl |
| TUS-21 | Admin Offer Type Configuration | Keep offer config in locale/currency context | BroadleafAdminRequestProcessor, AdminOfferController |
| TUS-22 | Admin Category Hierarchy Management | Persist parent-child category relationships | CategoryCustomPersistenceHandler, CategoryImpl |
| TUS-23 | Admin Category Hierarchy Management | Keep hierarchy aligned with the category aggregate | CategoryImpl, ProductImpl |
| TUS-24 | Admin Category Hierarchy Management | Run hierarchy saves through standard persistence stages | PersistenceManagerImpl, CategoryCustomPersistenceHandler |
| TUS-25 | Admin Product Persistence Rules | Apply product-specific persistence rules | ProductCustomPersistenceHandler, PersistenceManagerImpl |
| TUS-26 | Admin Product Persistence Rules | Handle product-category assignments consistently | ProductCustomPersistenceHandler, ProductImpl |
| TUS-27 | Admin Product Persistence Rules | Keep product saves sandbox-aware | BroadleafAdminRequestProcessor, ProductCustomPersistenceHandler |
| TUS-28 | Customer Password Update Validation | Validate customer password changes | CustomerPasswordCustomPersistenceHandler, PersistenceManagerImpl |
| TUS-29 | Customer Password Update Validation | Stop invalid password updates before persist | CustomerPasswordCustomPersistenceHandler, PersistenceManagerImpl |
| TUS-30 | Customer Password Update Validation | Align password updates with the customer aggregate | CustomerImpl, OrderImpl |

## Stories by epic

### Catalog

#### Admin Product Management

- **Epic ID:** `product-mgmt`
- **Domain:** Catalog
- **Stories:** 3
- **Summary:** Server-rendered product list/detail, shared persistence saves, and related SKU/category/option data.

##### TUS-01 — Open product list and detail views

**Title:** As an Admin User, I want to open the admin product list and product detail views, so that I can manage catalog products through the server-rendered admin interface.

**Summary:** Use the admin entity-controller pipeline and product-specific routes rather than a separate API-only flow.

**Involved:** `AdminBaseProductController`, `AdminBasicEntityController`, `AdminEntityService`

##### TUS-02 — Save products through the shared persistence pipeline

**Title:** As an Admin User, I want product saves to flow through the generic admin persistence pipeline, so that add and update operations use the platform’s standard metadata, security, validation, and deferred-operation handling.

**Summary:** Product add/update must go through PersistenceManagerImpl rather than bypassing the open-admin pipeline.

**Involved:** `AdminBasicEntityController`, `AdminEntityServiceImpl`, `PersistenceManagerImpl`

##### TUS-03 — Edit related SKUs, categories, and options

**Title:** As an Admin User, I want product editing to support related catalog data such as SKUs, categories, and product options through existing product domain relationships, so that product maintenance aligns with the underlying catalog model.

**Summary:** Treat ProductImpl as an aggregate with default/additional SKUs, category assignments, and option xrefs.

**Involved:** `ProductImpl`, `SkuImpl`, `CategoryImpl`

#### Admin Product Persistence Rules

- **Epic ID:** `product-persist`
- **Domain:** Catalog
- **Stories:** 3
- **Summary:** Product-specific custom persistence, category assignment rules, and sandbox-aware catalog saves.

##### TUS-25 — Apply product-specific persistence rules

**Title:** As an Admin User, I want product create and update operations to apply product-specific backend persistence rules, so that product catalog changes are processed consistently with custom product handling.

**Summary:** ProductCustomPersistenceHandler supplies product-specific validation and persistence behavior.

**Involved:** `ProductCustomPersistenceHandler`, `PersistenceManagerImpl`, `AdminBaseProductController`

##### TUS-26 — Handle product-category assignments consistently

**Title:** As an Admin User, I want product persistence to handle category-related product data consistently with the existing product and category model, so that admin changes to product catalog assignments follow the catalog constraints enforced by backend logic.

**Summary:** Product persistence must honor default category and category assignment relationships.

**Involved:** `ProductCustomPersistenceHandler`, `ProductImpl`, `CategoryImpl`

##### TUS-27 — Keep product saves sandbox-aware

**Title:** As an Admin User, I want product saves to remain sandbox-aware within the admin runtime, so that product updates respect the admin request context and backend rules already used for catalog management.

**Summary:** Product persistence runs after profile, catalog, and sandbox/admin state are resolved.

**Involved:** `BroadleafAdminRequestProcessor`, `ProductCustomPersistenceHandler`, `AdminBaseProductController`

#### Admin Category Hierarchy Management

- **Epic ID:** `category`
- **Domain:** Catalog
- **Stories:** 3
- **Summary:** Parent-child category persistence inside the shared admin pipeline, aligned with the catalog aggregate.

##### TUS-22 — Persist parent-child category relationships

**Title:** As an Admin User, I want category create and update operations to persist parent-child relationships, so that catalog hierarchy changes are saved consistently in the admin interface.

**Summary:** Category persistence is hierarchy-aware via CategoryCustomPersistenceHandler, not a flat record save.

**Involved:** `CategoryCustomPersistenceHandler`, `CategoryImpl`, `PersistenceManagerImpl`

##### TUS-23 — Keep hierarchy aligned with the category aggregate

**Title:** As an Admin User, I want category hierarchy management to remain consistent with category-to-product relationships already modeled in the catalog, so that parent-child edits do not bypass the wider category aggregate.

**Summary:** Parent-child edits must stay consistent with product xrefs and related merchandising data.

**Involved:** `CategoryImpl`, `ProductImpl`, `CategoryCustomPersistenceHandler`

##### TUS-24 — Run hierarchy saves through standard persistence stages

**Title:** As an Admin User, I want category hierarchy saves to execute through the standard admin persistence stages, so that security checks, validation, and deferred operations still apply to hierarchy changes.

**Summary:** Custom category handling is one stage inside PersistenceManagerImpl, not a bypass.

**Involved:** `PersistenceManagerImpl`, `CategoryCustomPersistenceHandler`, `AdminBasicEntityController`

### Orders

#### Admin Order Management

- **Epic ID:** `orders`
- **Domain:** Orders
- **Stories:** 3
- **Summary:** Order list/detail navigation, order-aggregate inspection, and to-one relationship resolution.

##### TUS-04 — Open order list and detail views

**Title:** As an Admin User, I want to open order management views in the admin area, so that order records can be navigated through the admin entity interface.

**Summary:** Order navigation uses AdminOrderController plus the generic admin list/detail stack.

**Involved:** `AdminOrderController`, `AdminBasicEntityController`, `AdminEntityService`

##### TUS-05 — Inspect items, fulfillment, payments, and offers

**Title:** As an Admin User, I want order detail pages to expose related order structures such as items, fulfillment groups, payments, and offer information from the existing order aggregate, so that I can inspect operationally relevant order data in context.

**Summary:** Order detail should reflect OrderImpl relationships rather than a single flat record.

**Involved:** `OrderImpl`, `OrderItemImpl`, `FulfillmentGroupImpl`, `OrderPaymentImpl`

##### TUS-06 — Resolve linked to-one order records

**Title:** As an Admin User, I want related to-one references in order administration to resolve through the admin entity-service relationship mechanisms, so that linked order records can be navigated consistently from the order section.

**Summary:** Use AdminEntityService context-specific relationship IDs for linked order entities.

**Involved:** `AdminOrderController`, `AdminEntityService`, `AdminEntityServiceImpl`

### Promotions

#### Admin Offer Type Configuration

- **Epic ID:** `offer-config`
- **Domain:** Promotions
- **Stories:** 3
- **Summary:** Offer list/add/duplicate routes, type-specific persistence handling, and locale/currency context.

##### TUS-19 — Open offer list, detail, add, and duplicate flows

**Title:** As an Admin User, I want to open offer list, detail, add, and duplicate flows in the admin interface, so that offer configuration is managed through the existing offer administration routes.

**Summary:** Offer administration is a server-rendered flow through AdminOfferController.

**Involved:** `AdminOfferController`, `AdminBasicEntityController`, `BroadleafAdminRequestProcessor`

##### TUS-20 — Apply type-specific offer persistence behavior

**Title:** As an Admin User, I want offer persistence to apply offer type-related and operation-specific configuration behavior through the offer custom persistence handler, so that the admin UI presents and saves contextual offer settings consistently.

**Summary:** OfferCustomPersistenceHandler drives type-related fields and operation-specific presentation.

**Involved:** `OfferCustomPersistenceHandler`, `PersistenceManagerImpl`, `AdminOfferController`

##### TUS-21 — Keep offer config in locale/currency context

**Title:** As an Admin User, I want offer configuration to remain contextual to locale and currency state already established in the request context, so that offer management aligns with the admin runtime context resolved before controller execution.

**Summary:** Offer admin runs after request processors resolve admin, locale, and currency state.

**Involved:** `BroadleafAdminRequestProcessor`, `AdminOfferController`, `BroadleafRequestProcessor`

#### Offer Code Uniqueness Validation

- **Epic ID:** `offer-unique`
- **Domain:** Promotions
- **Stories:** 3
- **Summary:** Reject duplicate codes, surface failures through the save pipeline, and keep order-to-code links unambiguous.

##### TUS-13 — Reject duplicate offer codes

**Title:** As an Admin User, I want offer-code saves to reject duplicate codes, so that each saved offer code remains unique.

**Summary:** OfferCodeCustomPersistenceHandler validates uniqueness on create and update.

**Involved:** `OfferCodeCustomPersistenceHandler`, `PersistenceManagerImpl`

##### TUS-14 — Surface duplicate-code errors in the save flow

**Title:** As an Admin User, I want duplicate offer-code errors to be surfaced through the existing admin save pipeline, so that failed offer-code saves behave like other admin validation failures.

**Summary:** Failed uniqueness checks must stop persistence and return through the standard admin save path.

**Involved:** `AdminBasicEntityController`, `PersistenceManagerImpl`, `OfferCodeCustomPersistenceHandler`

##### TUS-15 — Keep order-to-offer-code relationships unambiguous

**Title:** As an Admin User, I want offer-code uniqueness enforcement to preserve unambiguous order-to-offer-code relationships, so that promotion code management remains consistent with the existing order and offer domain model.

**Summary:** Uniqueness protects OfferCodeImpl links to OfferImpl and OrderImpl.

**Involved:** `OfferCodeImpl`, `OfferImpl`, `OrderImpl`

### CMS

#### Admin Static Asset Management

- **Epic ID:** `assets`
- **Domain:** CMS
- **Stories:** 3
- **Summary:** CMS asset CRUD through the admin console, CMS persistence-unit routing, and site-aware request state.

##### TUS-10 — Create and edit CMS static assets

**Title:** As an Admin User, I want to create and edit CMS static assets through the admin console, so that uploaded files and media can be managed within the existing admin/CMS controller framework.

**Summary:** AdminAssetController exposes CMS asset administration through the existing admin MVC flow.

**Involved:** `AdminAssetController`, `BroadleafAdminRequestProcessor`, `PersistenceManagerImpl`

##### TUS-11 — Route assets to the CMS persistence unit

**Title:** As an Admin User, I want static asset administration to route to the correct persistence context for CMS storage-backed entities, so that asset records are managed using the platform’s persistence-unit selection behavior.

**Summary:** CMS entities use persistence-cms.xml; do not assume the default EntityManager.

**Involved:** `PersistenceServiceImpl`, `PersistenceManagerImpl`, `persistence-cms.xml`

##### TUS-12 — Keep asset admin site-aware

**Title:** As an Admin User, I want static asset records to remain manageable in a site-aware admin context, so that CMS asset administration follows the same admin request-state handling used elsewhere in the admin console.

**Summary:** Asset requests participate in admin preprocessing for user, profile, catalog, and sandbox state.

**Involved:** `BroadleafAdminRequestProcessor`, `AdminAssetController`

### Security

#### Populate Request Type Validation

- **Epic ID:** `type-validation`
- **Domain:** Security
- **Stories:** 3
- **Summary:** Pre-population boolean/numeric checks that hard-stop malformed values across all metadata-driven saves.

##### TUS-07 — Validate boolean and numeric field types

**Title:** As an Admin User, I want incoming string field values to be validated against expected boolean and numeric types before population into persisted entities, so that invalid admin input is rejected before it mutates domain objects.

**Summary:** PopulateValueRequestValidator must reject mismatched string values before entity population.

**Involved:** `PopulateValueRequestValidator`, `PersistenceManagerImpl`

##### TUS-08 — Stop persistence on type-validation failure

**Title:** As an Admin User, I want type-validation failures to stop the admin persistence flow before domain objects are updated, so that malformed values never become part of the persisted entity state.

**Summary:** Validation is a hard stop in the persistence preparation sequence, not a warning after mutation.

**Involved:** `PopulateValueRequestValidator`, `PersistenceManagerImpl`, `AdminBasicEntityController`

##### TUS-09 — Apply type checks on every admin section save

**Title:** As an Admin User, I want type validation to run consistently across metadata-driven admin entity saves, so that boolean and numeric format checks are enforced regardless of which admin section submits the values.

**Summary:** Keep the rule on the shared admin persistence path, not duplicated in feature controllers.

**Involved:** `AdminBasicEntityController`, `PersistenceManagerImpl`, `PopulateValueRequestValidator`

#### Admin User Self-Protection And Password Validation

- **Epic ID:** `admin-user`
- **Domain:** Security
- **Stories:** 3
- **Summary:** Admin password rules, block self-delete/self-modification, and keep mutations in the secured persistence path.

##### TUS-16 — Enforce admin password validation

**Title:** As an Admin User, I want admin-user create and update operations to enforce password validation, so that invalid admin passwords are not accepted during admin-user maintenance.

**Summary:** Admin-user persistence includes password-focused validation during create and update.

**Involved:** `AdminUserCustomPersistenceHandler`, `PersistenceManagerImpl`, `AdminUserManagementController`

##### TUS-17 — Block unsafe self-modification

**Title:** As an Admin User, I want unsafe operations against the currently authenticated admin account to be blocked, so that I cannot self-delete or perform other disallowed self-modifications that would compromise access or security.

**Summary:** The admin-user persistence path must prevent self-delete and other protected self-changes.

**Involved:** `AdminUserCustomPersistenceHandler`, `BroadleafAdminRequestProcessor`, `PersistenceManagerImpl`

##### TUS-18 — Keep admin-user mutations in the secured pipeline

**Title:** As an Admin User, I want admin-user save and delete attempts to remain inside the secured admin persistence pipeline, so that validation and self-protection rules are enforced before any admin-user record is changed.

**Summary:** Admin-user mutations use standard persistence stages, including security checks and custom handlers.

**Involved:** `PersistenceManagerImpl`, `AdminUserCustomPersistenceHandler`, `AdminUserManagementController`

#### Customer Password Update Validation

- **Epic ID:** `customer-pw`
- **Domain:** Security
- **Stories:** 3
- **Summary:** Custom customer password validation that rejects invalid updates before they touch the customer aggregate.

##### TUS-28 — Validate customer password changes

**Title:** As an Admin User, I want customer password changes to be validated through custom customer persistence logic, so that only valid password update requests are accepted during customer maintenance.

**Summary:** CustomerPasswordCustomPersistenceHandler gates password updates during customer admin maintenance.

**Involved:** `CustomerPasswordCustomPersistenceHandler`, `PersistenceManagerImpl`, `CustomerImpl`

##### TUS-29 — Stop invalid password updates before persist

**Title:** As an Admin User, I want invalid customer password update attempts to stop before customer records are changed, so that the admin system does not persist rejected password changes.

**Summary:** Validation failure must prevent downstream persistence from applying the password change.

**Involved:** `CustomerPasswordCustomPersistenceHandler`, `PersistenceManagerImpl`, `AdminBasicEntityController`

##### TUS-30 — Align password updates with the customer aggregate

**Title:** As an Admin User, I want customer password validation to operate on the existing customer entity model used by order and profile data, so that password maintenance stays aligned with the platform’s customer aggregate.

**Summary:** Password handling targets CustomerImpl, the account entity already linked to orders.

**Involved:** `CustomerImpl`, `OrderImpl`, `CustomerPasswordCustomPersistenceHandler`
