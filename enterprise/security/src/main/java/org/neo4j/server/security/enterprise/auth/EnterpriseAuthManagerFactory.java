/*
 * Copyright (c) 2002-2016 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.server.security.enterprise.auth;

import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.helpers.Service;
import org.neo4j.kernel.api.security.AuthManager;
import org.neo4j.kernel.configuration.Config;
import org.neo4j.logging.LogProvider;
import org.neo4j.server.security.auth.BasicPasswordPolicy;
import org.neo4j.server.security.auth.FileUserRepository;
import org.neo4j.server.security.auth.PasswordPolicy;
import org.neo4j.server.security.auth.UserRepository;

import static java.time.Clock.systemUTC;

/**
 * Wraps EnterpriseAuthManager and exposes it as a Service
 */
@Service.Implementation( AuthManager.Factory.class )
public class EnterpriseAuthManagerFactory extends AuthManager.Factory
{
    public EnterpriseAuthManagerFactory()
    {
        super( "enterprise-auth-manager" );
    }

    @Override
    public AuthManager newInstance( Config config, LogProvider logProvider )
    {
        final UserRepository userRepository =
                new FileUserRepository( config.get( GraphDatabaseSettings.auth_store ).toPath(), logProvider );

        final PasswordPolicy passwordPolicy = new BasicPasswordPolicy();

        return new EnterpriseAuthManager( userRepository, passwordPolicy, systemUTC(),
                config.get( GraphDatabaseSettings.auth_enabled ) );
    }
}
