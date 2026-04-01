/*-
 * #%L
 * BroadleafCommerce Common Libraries
 * %%
 * Copyright (C) 2009 - 2026 Broadleaf Commerce
 * %%
 * Licensed under the Broadleaf Fair Use License Agreement, Version 1.0
 * (the "Fair Use License" located  at http://license.broadleafcommerce.org/fair_use_license-1.0.txt)
 * unless the restrictions on use therein are violated and require payment to Broadleaf in which case
 * the Broadleaf End User License Agreement (EULA), Version 1.1
 * (the "Commercial License" located at http://license.broadleafcommerce.org/commercial_license-1.1.txt)
 * shall apply.
 *
 * Alternatively, the Commercial License may be replaced with a mutually agreed upon license (the "Custom License")
 * between you and Broadleaf Commerce. You may not use this file except in compliance with the applicable license.
 * #L%
 */
package org.broadleafcommerce.common.dialect;

import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.dialect.DatabaseVersion;

/**
 * This custom dialect will treat all Clob types as if they contain a string instead of an OID.
 *
 * https://github.com/hibernate/hibernate-orm/wiki/Migration-Guide---5.2#changes-to-how-clob-values-are-processed-using-postgresql81dialect-and-its-subclasses
 *
 * Note: In Hibernate 7, the type descriptor system has been completely redesigned.
 * CLOB types are now handled differently and the getSqlTypeDescriptorOverride method no longer exists.
 * PostgreSQL automatically handles text/clob types appropriately in modern versions.
 */
public class BroadleafPostgreSQLDialect extends PostgreSQLDialect {

    public BroadleafPostgreSQLDialect() {
        super(DatabaseVersion.make(10));
    }

}
