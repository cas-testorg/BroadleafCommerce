# BroadleafCommerce user stories

CoreStory PRD for BroadleafCommerce — 42 product user stories across 9 epics, all written from the Admin User persona.

**Source:** product requirements document, retrieved 15 Sep 2026  
**CoreStory project:** BroadleafCommerce (`project_id` 603)  
**Canvas:** `user-stories.canvas.tsx`

| Metric | Value |
| --- | --- |
| User stories | 42 |
| Epics | 9 |
| Persona | Admin User |

## What these stories cover

These are product user stories for what the admin console is meant to do: catalog merchandising, order administration, promotions, CMS assets, and security rules. They sit in the PRD, separate from the 30 technical user stories that describe how those same epics are built.

## Stories by domain

Count of PRD user stories grouped by domain. Source: CoreStory PRD · BroadleafCommerce.

**PRD user stories by domain**

<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 420 140" role="img" aria-label="PRD user stories by domain: Catalog 15, Promotions 10, Security 8, CMS 5, Orders 4">
  <style>
    .domain-label { font: 12px system-ui, sans-serif; fill: currentColor; }
    .domain-count { font: 12px system-ui, sans-serif; fill: currentColor; font-variant-numeric: tabular-nums; }
    .domain-bar { fill: currentColor; opacity: 0.35; }
  </style>
  <text class="domain-label" x="0" y="16">Catalog</text>
  <rect class="domain-bar" x="88" y="4" width="240" height="16" rx="2" />
  <text class="domain-count" x="336" y="16">15</text>
  <text class="domain-label" x="0" y="42">Promotions</text>
  <rect class="domain-bar" x="88" y="30" width="160" height="16" rx="2" />
  <text class="domain-count" x="256" y="42">10</text>
  <text class="domain-label" x="0" y="68">Security</text>
  <rect class="domain-bar" x="88" y="56" width="128" height="16" rx="2" />
  <text class="domain-count" x="224" y="68">8</text>
  <text class="domain-label" x="0" y="94">CMS</text>
  <rect class="domain-bar" x="88" y="82" width="80" height="16" rx="2" />
  <text class="domain-count" x="176" y="94">5</text>
  <text class="domain-label" x="0" y="120">Orders</text>
  <rect class="domain-bar" x="88" y="108" width="64" height="16" rx="2" />
  <text class="domain-count" x="160" y="120">4</text>
</svg>

| Domain | Story count |
| --- | ---: |
| Catalog | 15 |
| Promotions | 10 |
| Security | 8 |
| CMS | 5 |
| Orders | 4 |

## Epic catalog

| Epic ID | Epic | Domain | Stories | Focus |
| --- | --- | --- | ---: | --- |
| `product-mgmt` | Admin Product Management | Catalog | 5 | Edit products, generate SKUs, manage categories/options, and show merchandising labels in the admin UI. |
| `product-persist` | Admin Product Persistence Rules | Catalog | 5 | Default SKU creation, preserve existing SKUs, sync category assignments, sandbox-aware saves, and extension hooks. |
| `category` | Admin Category Hierarchy Management | Catalog | 5 | Create and update parent-child categories, show hierarchy paths, and reject incomplete or invalid links. |
| `orders` | Admin Order Management | Orders | 4 | Open order records, follow fulfillment-oriented actions, resolve related-entity links, and tolerate missing targets. |
| `offer-config` | Admin Offer Type Configuration | Promotions | 5 | Type-specific offer fields, dynamic forms, targeting requirements, add-vs-edit presentation, and extension points. |
| `offer-unique` | Offer Code Uniqueness Validation | Promotions | 5 | Reject duplicates on create and update, allow keeping the current code, and return field-level offerCode errors. |
| `assets` | Admin Static Asset Management | CMS | 5 | Create/update CMS assets, image previews, storage strategy, URL normalization, and site-aware asset admin. |
| `type-validation` | Populate Request Type Validation | Security | 4 | Accept valid typed values; reject bad booleans, unparseable numbers, and mismatched types before entity populate. |
| `admin-user` | Admin User Self-Protection And Password Validation | Security | 4 | Password rules on create, safe self-updates, block self-delete, and permission checks when editing other admins. |

## All 42 stories

| ID | Epic | Want | References |
| --- | --- | --- | --- |
| US-01 | Admin Product Management | Edit a product record | AdminProductController.java, AdminBaseProductController.java |
| US-02 | Admin Product Management | Generate additional SKUs | AdminProductController.java, AdminBaseProductController.java |
| US-03 | Admin Product Management | Surface SKU generation failures | AdminProductController.java, AdminBaseProductController.java |
| US-04 | Admin Product Management | Manage categories and product options | AdminProductController.java, AdminBaseProductController.java |
| US-05 | Admin Product Management | Show merchandising-specific labels | MerchandisingMessages.properties, AdminProductController.java |
| US-06 | Admin Order Management | Open and manage an order record | AdminOrderController.java, OrderImpl.java |
| US-07 | Admin Order Management | Use fulfillment-oriented order actions | AdminOrderController.java, OrderServiceImpl.java |
| US-08 | Admin Order Management | Resolve links to related entities | AdminOrderController.java, OrderImpl.java |
| US-09 | Admin Order Management | Handle missing related-entity targets | AdminOrderController.java, OrderImpl.java |
| US-10 | Populate Request Type Validation | Accept valid typed field values | PopulateValueRequestValidator.java, BasicFieldTypeValidator.java |
| US-11 | Populate Request Type Validation | Reject invalid boolean text | PopulateValueRequestValidator.java, BasicFieldTypeValidator.java |
| US-12 | Populate Request Type Validation | Fail malformed numeric values | BasicFieldTypeValidator.java, PopulateValueRequestValidator.java |
| US-13 | Populate Request Type Validation | Stop mismatched type/value combinations | EntityValidatorServiceImpl.java, PopulateValueRequestValidator.java |
| US-14 | Admin Static Asset Management | Open and submit a static asset form | AdminAssetController.java, StaticAssetService.java |
| US-15 | Admin Static Asset Management | Show image thumbnails and previews | AdminAssetUploadController.java, AdminAssetController.java |
| US-16 | Admin Static Asset Management | Store assets with the configured strategy | StaticAssetService.java, StaticAssetServiceImpl.java |
| US-17 | Admin Static Asset Management | Resolve asset URLs with encodings or query strings | StaticAssetServiceImpl.java, AdminAssetUploadController.java |
| US-18 | Admin Static Asset Management | Respect site-aware asset rules | AdminAssetController.java |
| US-19 | Offer Code Uniqueness Validation | Reject duplicate new offer codes | OfferCodeCustomPersistenceHandler.java, UniqueValueValidator.java |
| US-20 | Offer Code Uniqueness Validation | Save unique new offer codes | OfferCodeCustomPersistenceHandler.java |
| US-21 | Offer Code Uniqueness Validation | Allow retaining the current code on edit | OfferCodeCustomPersistenceHandler.java |
| US-22 | Offer Code Uniqueness Validation | Fail edits that collide with another code | OfferCodeCustomPersistenceHandler.java |
| US-23 | Offer Code Uniqueness Validation | Return field-level offerCode errors | OfferCodeCustomPersistenceHandler.java, UniqueValueValidator.java |
| US-24 | Admin User Self-Protection And Password Validation | Reject invalid admin passwords on create | AdminUserCustomPersistenceHandler.java, adminUser.js |
| US-25 | Admin User Self-Protection And Password Validation | Allow safe self-maintenance | AdminUserCustomPersistenceHandler.java, AdminSecurityServiceRemote.java |
| US-26 | Admin User Self-Protection And Password Validation | Block deletion of my own account | AdminUserCustomPersistenceHandler.java, RowLevelSecurityProvider.java |
| US-27 | Admin User Self-Protection And Password Validation | Require permission to update other admins | AdminUserCustomPersistenceHandler.java, AdminSecurityServiceRemote.java |
| US-28 | Admin Offer Type Configuration | Save offer type-specific configuration | OfferCustomPersistenceHandler.java, AdminOfferController.java |
| US-29 | Admin Offer Type Configuration | Change the form by discount and offer type | AdminOfferController.java, offer.js |
| US-30 | Admin Offer Type Configuration | Enforce targeting requirements before save | AdminOfferController.java, offer.js |
| US-31 | Admin Offer Type Configuration | Apply add versus edit presentation consistently | AdminOfferController.java, OfferCustomPersistenceHandler.java |
| US-32 | Admin Offer Type Configuration | Extend offer administration without replacing the core flow | AdminOfferControllerExtensionHandler.java, OfferCustomPersistenceHandler.java |
| US-33 | Admin Category Hierarchy Management | Create a category under a parent | CategoryCustomPersistenceHandler.java, CategoryImpl.java |
| US-34 | Admin Category Hierarchy Management | Retain or change parent on update | CategoryCustomPersistenceHandler.java, AdminCategoryController.java |
| US-35 | Admin Category Hierarchy Management | Show the resolved hierarchy path | CategoryCustomPersistenceHandler.java |
| US-36 | Admin Category Hierarchy Management | Require both IDs before creating child links | ChildCategoriesCustomPersistenceHandler.java, CategoryCustomPersistenceHandler.java |
| US-37 | Admin Category Hierarchy Management | Fail safely on invalid hierarchy identifiers | CategoryCustomPersistenceHandler.java, ChildCategoriesCustomPersistenceHandler.java |
| US-38 | Admin Product Persistence Rules | Save a new product with a default SKU | ProductCustomPersistenceHandler.java |
| US-39 | Admin Product Persistence Rules | Preserve existing default SKUs | ProductCustomPersistenceHandler.java |
| US-40 | Admin Product Persistence Rules | Validate and sync category assignments | ProductCustomPersistenceHandler.java |
| US-41 | Admin Product Persistence Rules | Keep product saves sandbox-aware | ProductCustomPersistenceHandler.java |
| US-42 | Admin Product Persistence Rules | Support extension hooks on product save | ProductCustomPersistenceHandler.java |

## Stories by epic

### Catalog

#### Admin Product Management

- **Epic ID:** `product-mgmt`
- **Domain:** Catalog
- **Stories:** 5
- **Summary:** Edit products, generate SKUs, manage categories/options, and show merchandising labels in the admin UI.

##### US-01 — Edit a product record

**Title:** As an Admin User I want to edit a product record in the admin interface so that I can maintain core merchandising data.

**Summary:** The product admin controller exposes the product entity for editing using presentation metadata and the standard admin form workflow.

**References:** `AdminProductController.java`, `AdminBaseProductController.java`, `ProductAdminPresentation.java`

##### US-02 — Generate additional SKUs

**Title:** As an Admin User I want to generate additional SKUs for a product so that I can quickly create SKU combinations from the admin UI.

**Summary:** The product admin UI includes a custom action on the additional SKUs grid to generate SKU combinations from product maintenance.

**References:** `AdminProductController.java`, `AdminBaseProductController.java`

##### US-03 — Surface SKU generation failures

**Title:** As an Admin User I want product SKU generation failures to surface as admin errors so that I know when product maintenance did not complete successfully.

**Summary:** SKU-related or product-maintenance failures must propagate back through the admin workflow instead of failing silently.

**References:** `AdminProductController.java`, `AdminBaseProductController.java`

##### US-04 — Manage categories and product options

**Title:** As an Admin User I want to manage product relationships such as categories and options from the product admin experience so that related merchandising data stays synchronized.

**Summary:** Product management includes related entities such as categories and product options, not only core product fields.

**References:** `AdminProductController.java`, `AdminBaseProductController.java`, `ProductAdminPresentation.java`

##### US-05 — Show merchandising-specific labels

**Title:** As an Admin User I want the product admin screen to render merchandising-specific labels and module text so that the interface is understandable in the catalog management context.

**Summary:** Product administration should present actions using merchandising message resources and localized admin labels.

**References:** `MerchandisingMessages.properties`, `AdminProductController.java`

#### Admin Product Persistence Rules

- **Epic ID:** `product-persist`
- **Domain:** Catalog
- **Stories:** 5
- **Summary:** Default SKU creation, preserve existing SKUs, sync category assignments, sandbox-aware saves, and extension hooks.

##### US-38 — Save a new product with a default SKU

**Title:** As an Admin User I want a new product saved with a default SKU so that incomplete product records are still persisted in a valid catalog state

**Summary:** If a product has no default SKU, the custom handler creates and persists one before completing product save.

**References:** `ProductCustomPersistenceHandler.java`

##### US-39 — Preserve existing default SKUs

**Title:** As an Admin User I want existing products with valid default SKUs to save without replacement so that deliberate SKU assignments are preserved

**Summary:** Automatic SKU creation applies only when defaultSku is null; existing associations are not replaced on ordinary updates.

**References:** `ProductCustomPersistenceHandler.java`

##### US-40 — Validate and sync category assignments

**Title:** As an Admin User I want category assignments on a product save to be validated and synchronized so that product-to-category catalog data remains consistent

**Summary:** The product handler validates incoming category changes and persists a consistent product/category state.

**References:** `ProductCustomPersistenceHandler.java`

##### US-41 — Keep product saves sandbox-aware

**Title:** As an Admin User I want sandbox-aware product saves so that my admin changes follow the correct catalog lifecycle and approval model

**Summary:** In sandboxed admin contexts, product saves follow the catalog lifecycle instead of unrestricted updates.

**References:** `ProductCustomPersistenceHandler.java`

##### US-42 — Support extension hooks on product save

**Title:** As an Admin User I want product save logic to support extension hooks so that custom validation or persistence behavior can run without replacing the core handler

**Summary:** Registered extensions can participate in product create/update while the base handler still completes when none are present.

**References:** `ProductCustomPersistenceHandler.java`

#### Admin Category Hierarchy Management

- **Epic ID:** `category`
- **Domain:** Catalog
- **Stories:** 5
- **Summary:** Create and update parent-child categories, show hierarchy paths, and reject incomplete or invalid links.

##### US-33 — Create a category under a parent

**Title:** As an Admin User I want to create a catalog category and assign it a parent category so that it appears in the correct place in the hierarchy.

**Summary:** Create persists the category and, when a parent id is supplied, resolves and attaches the parent-child association.

**References:** `CategoryCustomPersistenceHandler.java`, `CategoryImpl.java`

##### US-34 — Retain or change parent on update

**Title:** As an Admin User I want category updates to retain or change parent relationships correctly so that hierarchy edits are saved reliably.

**Summary:** Update-specific parent management honors extension results when present, otherwise default custom persistence continues.

**References:** `CategoryCustomPersistenceHandler.java`, `AdminCategoryController.java`

##### US-35 — Show the resolved hierarchy path

**Title:** As an Admin User I want the admin interface to show the resolved category hierarchy path so that I can confirm where a category sits in the catalog tree.

**Summary:** Hierarchy link text is built from persisted category data after lookup, not from unsaved free-form input.

**References:** `CategoryCustomPersistenceHandler.java`

##### US-36 — Require both IDs before creating child links

**Title:** As an Admin User I want child category relationship records to be processed only when both category identifiers are present so that incomplete hierarchy submissions do not create invalid links.

**Summary:** The child-category handler requires both category id and subcategory id before creating a hierarchy association.

**References:** `ChildCategoriesCustomPersistenceHandler.java`, `CategoryCustomPersistenceHandler.java`

##### US-37 — Fail safely on invalid hierarchy identifiers

**Title:** As an Admin User I want category hierarchy updates to fail safely when submitted identifiers are invalid so that bad admin input does not silently corrupt catalog structure.

**Summary:** Unparseable or unresolved category ids stop hierarchy changes; existing valid hierarchy data remains unchanged.

**References:** `CategoryCustomPersistenceHandler.java`, `ChildCategoriesCustomPersistenceHandler.java`

### Orders

#### Admin Order Management

- **Epic ID:** `orders`
- **Domain:** Orders
- **Stories:** 4
- **Summary:** Open order records, follow fulfillment-oriented actions, resolve related-entity links, and tolerate missing targets.

##### US-06 — Open and manage an order record

**Title:** As an Admin User, I want to open and manage an order record from the admin order section so that I can review and administer a specific order.

**Summary:** A dedicated order admin controller extends generic entity handling for navigation into and administration of order records.

**References:** `AdminOrderController.java`, `OrderImpl.java`

##### US-07 — Use fulfillment-oriented order actions

**Title:** As an Admin User, I want related order actions to reflect fulfillment-oriented administration paths so that I can work on downstream order operations from the order section.

**Summary:** Order admin supports navigation to related fulfillment-oriented actions, not only static record viewing.

**References:** `AdminOrderController.java`, `OrderServiceImpl.java`

##### US-08 — Resolve links to related entities

**Title:** As an Admin User, I want the order admin UI to resolve external links for related to-one entities so that I can navigate from an order to its associated customer record.

**Summary:** Orders expose a to-one customer relationship; the admin section resolves external links for related to-one entities.

**References:** `AdminOrderController.java`, `OrderImpl.java`

##### US-09 — Handle missing related-entity targets

**Title:** As an Admin User, I want the order admin screen to handle missing or invalid related-entity targets gracefully so that broken order relationships do not prevent me from managing the order.

**Summary:** The order can still be administered when a related entity target cannot be resolved or is absent.

**References:** `AdminOrderController.java`, `OrderImpl.java`

### Promotions

#### Admin Offer Type Configuration

- **Epic ID:** `offer-config`
- **Domain:** Promotions
- **Stories:** 5
- **Summary:** Type-specific offer fields, dynamic forms, targeting requirements, add-vs-edit presentation, and extension points.

##### US-28 — Save offer type-specific configuration

**Title:** As an Admin User I want to save offer type-specific configuration so that the correct contextual fields are persisted for an offer

**Summary:** Create/update persists type-related values and contextual settings such as currency, locale, and visibility.

**References:** `OfferCustomPersistenceHandler.java`, `AdminOfferController.java`

##### US-29 — Change the form by discount and offer type

**Title:** As an Admin User I want the offer form to change based on discount and offer type selections so that I only see relevant configuration inputs

**Summary:** The offer screen shows, hides, and interprets fields according to the selected discount type and related settings.

**References:** `AdminOfferController.java`, `offer.js`

##### US-30 — Enforce targeting requirements before save

**Title:** As an Admin User I want targeting-related requirements to be enforced in the offer form so that incomplete offer definitions are prevented before save

**Summary:** For some offer scenarios, targeting criteria are required at the form level even when the entity model cannot express that rule.

**References:** `AdminOfferController.java`, `offer.js`

##### US-31 — Apply add versus edit presentation consistently

**Title:** As an Admin User I want offer visibility and presentation behavior to be applied consistently for add versus edit operations so that the admin UI reflects the correct offer context

**Summary:** Form presentation adapts to add versus edit and respects visibility-related settings for the current offer.

**References:** `AdminOfferController.java`, `OfferCustomPersistenceHandler.java`, `AdminOfferControllerExtensionHandler.java`

##### US-32 — Extend offer administration without replacing the core flow

**Title:** As an Admin User I want extensible offer administration behavior so that custom implementations can alter offer configuration handling without replacing the core flow

**Summary:** Controller and persistence extension points let custom projects alter offer handling while the default workflow still runs.

**References:** `AdminOfferControllerExtensionHandler.java`, `OfferCustomPersistenceHandler.java`

#### Offer Code Uniqueness Validation

- **Epic ID:** `offer-unique`
- **Domain:** Promotions
- **Stories:** 5
- **Summary:** Reject duplicates on create and update, allow keeping the current code, and return field-level offerCode errors.

##### US-19 — Reject duplicate new offer codes

**Title:** As an Admin User, I want a new offer code to be rejected when its code already exists so that I cannot create duplicate promotion codes.

**Summary:** On create, the custom persistence handler checks the requested code and blocks save when another offer code already uses that value.

**References:** `OfferCodeCustomPersistenceHandler.java`, `UniqueValueValidator.java`

##### US-20 — Save unique new offer codes

**Title:** As an Admin User, I want to save a new offer code when its code is unique so that valid promotion codes can be created successfully.

**Summary:** When the submitted code does not already exist, the handler continues through normal persistence and returns a successful save.

**References:** `OfferCodeCustomPersistenceHandler.java`

##### US-21 — Allow retaining the current code on edit

**Title:** As an Admin User, I want editing an existing offer code to allow retaining its current code value so that simple updates do not fail uniqueness validation.

**Summary:** Uniqueness checks treat the current record as non-conflicting so updates that keep the same code can succeed.

**References:** `OfferCodeCustomPersistenceHandler.java`

##### US-22 — Fail edits that collide with another code

**Title:** As an Admin User, I want editing an offer code to fail when I change it to a code already used by another offer so that duplicate promotion codes cannot be introduced later.

**Summary:** The same uniqueness rule applies on update when the new value belongs to a different offer-code record.

**References:** `OfferCodeCustomPersistenceHandler.java`

##### US-23 — Return field-level offerCode errors

**Title:** As an Admin User, I want duplicate-code failures to be returned as field-level validation errors on the offerCode property so that I can correct the exact input that caused the save to fail.

**Summary:** Failed uniqueness checks build an error on the offerCode property rather than throwing an unhandled persistence failure.

**References:** `OfferCodeCustomPersistenceHandler.java`, `UniqueValueValidator.java`

### CMS

#### Admin Static Asset Management

- **Epic ID:** `assets`
- **Domain:** CMS
- **Stories:** 5
- **Summary:** Create/update CMS assets, image previews, storage strategy, URL normalization, and site-aware asset admin.

##### US-14 — Open and submit a static asset form

**Title:** As an Admin User I want to open and submit a static asset form so that I can create or update a CMS asset in the admin console.

**Summary:** The admin asset controller handles CMS static-asset CRUD and delegates form construction to the asset form builder.

**References:** `AdminAssetController.java`, `StaticAssetService.java`

##### US-15 — Show image thumbnails and previews

**Title:** As an Admin User I want uploaded image assets to display with admin thumbnails and large previews so that I can verify I selected the correct media.

**Summary:** Image uploads return thumbnail and large preview URLs so administrators can verify the selected media.

**References:** `AdminAssetUploadController.java`, `AdminAssetController.java`

##### US-16 — Store assets with the configured strategy

**Title:** As an Admin User I want the system to store static assets using the configured storage strategy so that asset administration works across different deployment environments.

**Summary:** Static asset storage, including filesystem-backed storage, follows environment-driven configuration.

**References:** `StaticAssetService.java`, `StaticAssetServiceImpl.java`

##### US-17 — Resolve asset URLs with encodings or query strings

**Title:** As an Admin User I want asset URLs to be resolved consistently even when request URLs contain encodings or query strings so that I can manage the correct stored asset.

**Summary:** Incoming full URLs are decoded and query strings stripped before lookup so encoded or parameterized URLs still match stored assets.

**References:** `StaticAssetServiceImpl.java`, `AdminAssetUploadController.java`

##### US-18 — Respect site-aware asset rules

**Title:** As an Admin User I want asset administration to respect site-aware or multi-tenant rules so that I only manage assets in the correct administrative context.

**Summary:** Asset administration uses the static-asset multi-tenant extension manager so admins stay in the correct site context.

**References:** `AdminAssetController.java`

### Security

#### Populate Request Type Validation

- **Epic ID:** `type-validation`
- **Domain:** Security
- **Stories:** 4
- **Summary:** Accept valid typed values; reject bad booleans, unparseable numbers, and mismatched types before entity populate.

##### US-10 — Accept valid typed field values

**Title:** As a Admin User I want valid typed field values to be accepted during populate so my changes can be applied to the target entity

**Summary:** When submitted string values match the expected field type, populate validation allows the value through persistence.

**References:** `PopulateValueRequestValidator.java`, `BasicFieldTypeValidator.java`, `PopulateValueRequest.java`

##### US-11 — Reject invalid boolean text

**Title:** As a Admin User I want invalid boolean text to be rejected before population so that bad admin data is not written to entities

**Summary:** Boolean fields must reject strings that are not valid boolean representations before entity population.

**References:** `PopulateValueRequestValidator.java`, `BasicFieldTypeValidator.java`, `PopulateValueRequest.java`

##### US-12 — Fail malformed numeric values

**Title:** As a Admin User I want malformed numeric values to fail validation so that numeric fields only receive parseable data

**Summary:** Integer, long, and similar numeric fields parse-check the string before populate and reject unparseable values.

**References:** `BasicFieldTypeValidator.java`, `PopulateValueRequestValidator.java`, `PopulateValueRequest.java`

##### US-13 — Stop mismatched type/value combinations

**Title:** As a Admin User I want unsupported or mismatched type/value combinations to be stopped consistently across update validation so partial admin submissions do not corrupt existing entities

**Summary:** On updates, submitted properties with type mismatches are rejected before they can partially corrupt existing entity state.

**References:** `EntityValidatorServiceImpl.java`, `PopulateValueRequestValidator.java`, `PopulateValueRequest.java`

#### Admin User Self-Protection And Password Validation

- **Epic ID:** `admin-user`
- **Domain:** Security
- **Stories:** 4
- **Summary:** Password rules on create, safe self-updates, block self-delete, and permission checks when editing other admins.

##### US-24 — Reject invalid admin passwords on create

**Title:** As an Admin User I want new admin accounts to be rejected when the password does not meet required validation rules

**Summary:** Create stops when the password is blank, malformed, or otherwise fails configured validation.

**References:** `AdminUserCustomPersistenceHandler.java`, `adminUser.js`

##### US-25 — Allow safe self-maintenance

**Title:** As an Admin User I want my own account updates to avoid inappropriate cross-user security checks while still allowing safe self-maintenance

**Summary:** Updating the authenticated admin’s own record skips the remote update check used when changing other users.

**References:** `AdminUserCustomPersistenceHandler.java`, `AdminSecurityServiceRemote.java`

##### US-26 — Block deletion of my own account

**Title:** As an Admin User I want the system to block deletion of my own admin account so I cannot lock myself out

**Summary:** The currently authenticated admin cannot delete their own account, protecting access continuity.

**References:** `AdminUserCustomPersistenceHandler.java`, `RowLevelSecurityProvider.java`

##### US-27 — Require permission to update other admins

**Title:** As an Admin User I want updates to other admin accounts to require explicit update permission checks

**Summary:** Modifying another admin user maps to UPDATE permission through remote security enforcement.

**References:** `AdminUserCustomPersistenceHandler.java`, `AdminSecurityServiceRemote.java`

