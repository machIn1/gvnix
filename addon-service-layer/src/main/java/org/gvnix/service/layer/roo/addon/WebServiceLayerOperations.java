/*
 * gvNIX. Spring Roo based RAD tool for Conselleria d'Infraestructures
 * i Transport - Generalitat Valenciana
 * Copyright (C) 2010 CIT - Generalitat Valenciana
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.gvnix.service.layer.roo.addon;

import org.springframework.roo.model.JavaType;

/**
 * Addon for Handle Service Layer
 * 
 * @author Ricardo García ( rgarcia at disid dot com ) at <a
 *         href="http://www.disid.com">DiSiD Technologies S.L.</a> made for <a
 *         href="http://www.cit.gva.es">Conselleria d'Infraestructures i
 *         Transport</a>
 */
public interface WebServiceLayerOperations {

    boolean isProjectAvailable();

    /**
     * <p>
     * Exports a class to Web Service.
     * </p>
     * 
     * @param className
     *            class to export.
     */
    public void exportService(JavaType className);

    /**
     * <p>
     * Create a Service class.
     * </p>
     * 
     * @param serviceClass
     *            class to be created.
     */
    public void createServiceClass(JavaType serviceClass);

    /**
     * <p>
     * Update an existing class to a web service.
     * </p>
     * 
     * @param serviceClass
     *            class to be published as Web Service.
     */
    public void updateClassAsWebService(JavaType serviceClass);

    /**
     * <p>
     * Check if Cxf config file is created in the project.
     * </p>
     * 
     * @return true or false if exists Cxf configuration file.
     */
    public boolean isCxfConfigurated();

    /**
     * <p>
     * Adds GvNIX annotations library dependency to the current project
     * </p>
     * TO BE REMOVED FROM API
     */
    public void addGvNIXAnnotationsDependecy();

}
