package org.gvnix.addon.loupefield;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.gvnix.support.MessageBundleUtils;
import org.gvnix.support.WebProjectUtils;
import org.gvnix.support.dependenciesmanager.DependenciesVersionManager;
import org.gvnix.web.i18n.roo.addon.ValencianCatalanLanguage;
import org.springframework.roo.addon.propfiles.PropFileOperations;
import org.springframework.roo.addon.web.mvc.controller.scaffold.WebScaffoldAnnotationValues;
import org.springframework.roo.addon.web.mvc.jsp.i18n.I18n;
import org.springframework.roo.addon.web.mvc.jsp.i18n.I18nSupport;
import org.springframework.roo.addon.web.mvc.jsp.i18n.languages.SpanishLanguage;
import org.springframework.roo.classpath.TypeLocationService;
import org.springframework.roo.classpath.TypeManagementService;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetailsBuilder;
import org.springframework.roo.classpath.details.MemberFindingUtils;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.metadata.MetadataService;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.process.manager.FileManager;
import org.springframework.roo.process.manager.MutableFile;
import org.springframework.roo.project.LogicalPath;
import org.springframework.roo.project.Path;
import org.springframework.roo.project.PathResolver;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.project.Property;
import org.springframework.roo.project.Repository;
import org.springframework.roo.support.logging.HandlerUtils;
import org.springframework.roo.support.util.FileUtils;
import org.springframework.roo.support.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Implementation of operations this add-on offers.
 * 
 * @since 1.1
 */
@Component
// Use these Apache Felix annotations to register your commands class in the Roo
// container
@Service
public class LoupefieldOperationsImpl implements LoupefieldOperations {

    @Reference
    private FileManager fileManager;

    @Reference
    private PathResolver pathResolver;

    @Reference
    private I18nSupport i18nSupport;

    @Reference
    private ProjectOperations projectOperations;

    @Reference
    private PropFileOperations propFileOperations;

    @Reference
    private TypeLocationService typeLocationService;

    @Reference
    private TypeManagementService typeManagementService;

    @Reference
    private MetadataService metadataService;

    private static final Logger LOGGER = HandlerUtils
            .getLogger(LoupefieldOperationsImpl.class);

    private static final JavaType ANNOTATION_LOUPE_CONTROLLER = new JavaType(
            "org.gvnix.addon.loupefield.GvNIXLoupeController");

    /** {@inheritDoc} */
    public boolean isSetupCommandAvailable() {
        // If jQuery is installed, setup command is available
        return projectOperations
                .isFeatureInstalledInFocusedModule("gvnix-jquery")
                && !projectOperations
                        .isFeatureInstalledInFocusedModule("gvnix-loupe");
    }

    /** {@inheritDoc} */
    public boolean isSetCommandAvailable() {
        // If loupefields addon is installed, set command is available
        return projectOperations
                .isFeatureInstalledInFocusedModule("gvnix-loupe");
    }

    /** {@inheritDoc} */
    public boolean isUpdateCommandAvailable() {
        // If loupefields addon is installed, update command is available
        return projectOperations
                .isFeatureInstalledInFocusedModule("gvnix-loupe");
    }

    /** {@inheritDoc} */
    public void setup() {
        // Adding tags/loupefield/select.tagx
        addTagx();
        // Adding scripts/loupefield/loupe-functions.js
        addLoupeFunctions();
        // Add necessary properties to messages.properties
        addI18nProperties();
        // Include loupe-functions.js into load-scripts.tagx
        addToLoadScripts();
        // Add Necessary Dependencies
        setupProjectPom();
    }

    /** {@inheritDoc} */
    public void setLoupeController(JavaType controller) {
        Validate.notNull(controller, "Controller Java Type required");
        // Adding annotation to Controller
        doAddControllerAnnotation(controller);
    }

    /** {@inheritDoc} */
    public void update() {
        // Adding tags/loupefield/select.tagx
        updateTagx();
        // Adding scripts/loupefield/loupe-functions.js
        updateLoupeFunctions();
        // Add necessary properties to messages.properties
        addI18nProperties();
        // Include loupe-functions.js into load-scripts.tagx
        addToLoadScripts();
        // Add Necessary Dependencies
        setupProjectPom();
    }

    /**
     * This method adds <code>tags/loupefield/loupe.tagx</code> to the tags
     * folder
     */
    public void addTagx() {

        final String filePath = pathResolver.getFocusedIdentifier(
                Path.SRC_MAIN_WEBAPP, "WEB-INF/tags/loupefield/loupe.tagx");

        if (!fileManager.exists(filePath)) {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                inputStream = FileUtils.getInputStream(getClass(),
                        "tag/loupe.tagx");
                outputStream = fileManager.createFile(filePath)
                        .getOutputStream();
                IOUtils.copy(inputStream, outputStream);
            }
            catch (final IOException ioe) {
                throw new IllegalStateException(ioe);
            }
            finally {
                IOUtils.closeQuietly(inputStream);
                IOUtils.closeQuietly(outputStream);
            }
        }
    }

    /**
     * This method update <code>tags/loupefield/select.tagx</code> with the
     * current tagx version
     */
    public void updateTagx() {

        final String filePath = pathResolver.getFocusedIdentifier(
                Path.SRC_MAIN_WEBAPP, "WEB-INF/tags/loupefield/loupe.tagx");

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = FileUtils
                    .getInputStream(getClass(), "tag/loupe.tagx");
            if (!fileManager.exists(filePath)) {
                outputStream = fileManager.createFile(filePath)
                        .getOutputStream();
            }
            else {
                outputStream = fileManager.updateFile(filePath)
                        .getOutputStream();
            }
            IOUtils.copy(inputStream, outputStream);
        }
        catch (final IOException ioe) {
            throw new IllegalStateException(ioe);
        }
        finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }

    /**
     * This method adds <code>scripts/loupefield/loupe-functions.js</code> to
     * the scripts folder
     */
    public void addLoupeFunctions() {
        final String filePath = pathResolver.getFocusedIdentifier(
                Path.SRC_MAIN_WEBAPP, "scripts/loupefield/loupe-functions.js");

        if (!fileManager.exists(filePath)) {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                inputStream = FileUtils.getInputStream(getClass(),
                        "scripts/loupe-functions.js");
                outputStream = fileManager.createFile(filePath)
                        .getOutputStream();
                IOUtils.copy(inputStream, outputStream);
            }
            catch (final IOException ioe) {
                throw new IllegalStateException(ioe);
            }
            finally {
                IOUtils.closeQuietly(inputStream);
                IOUtils.closeQuietly(outputStream);
            }
        }
    }

    /**
     * This method updates <code>scripts/loupefield/loupe-functions.js</code>
     * with the current version
     */
    public void updateLoupeFunctions() {
        final String filePath = pathResolver.getFocusedIdentifier(
                Path.SRC_MAIN_WEBAPP, "scripts/loupefield/loupe-functions.js");

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = FileUtils.getInputStream(getClass(),
                    "scripts/loupe-functions.js");
            if (!fileManager.exists(filePath)) {
                outputStream = fileManager.createFile(filePath)
                        .getOutputStream();
            }
            else {
                outputStream = fileManager.updateFile(filePath)
                        .getOutputStream();
            }
            IOUtils.copy(inputStream, outputStream);
        }
        catch (final IOException ioe) {
            throw new IllegalStateException(ioe);
        }
        finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }

    /**
     * This method add necessary properties to messages.properties
     */
    public void addI18nProperties() {
        // Check if Valencian_Catalan language is supported and add properties
        // if so
        Set<I18n> supportedLanguages = i18nSupport.getSupportedLanguages();
        for (I18n i18n : supportedLanguages) {
            if (i18n.getLocale().equals(new Locale("ca"))) {
                MessageBundleUtils.installI18nMessages(
                        new ValencianCatalanLanguage(), projectOperations,
                        fileManager);
                MessageBundleUtils.addPropertiesToMessageBundle("ca",
                        getClass(), propFileOperations, projectOperations,
                        fileManager);
                break;
            }
        }
        // Add properties to Spanish messageBundle
        MessageBundleUtils.installI18nMessages(new SpanishLanguage(),
                projectOperations, fileManager);
        MessageBundleUtils.addPropertiesToMessageBundle("es", getClass(),
                propFileOperations, projectOperations, fileManager);

        // Add properties to default messageBundle
        MessageBundleUtils.addPropertiesToMessageBundle("en", getClass(),
                propFileOperations, projectOperations, fileManager);
    }

    /**
     * This method adds reference in laod-script.tagx to use loupe-functions.js
     */
    public void addToLoadScripts() {
        // Modify Roo load-scripts.tagx
        PathResolver pathResolver = projectOperations.getPathResolver();
        String docTagxPath = pathResolver.getIdentifier(getWebappPath(),
                "WEB-INF/tags/util/load-scripts.tagx");

        Validate.isTrue(fileManager.exists(docTagxPath),
                "load-script.tagx not found: ".concat(docTagxPath));

        MutableFile docTagxMutableFile = null;
        Document docTagx;

        try {
            docTagxMutableFile = fileManager.updateFile(docTagxPath);
            docTagx = XmlUtils.getDocumentBuilder().parse(
                    docTagxMutableFile.getInputStream());
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
        Element root = docTagx.getDocumentElement();

        boolean modified = false;

        // Add loupe-functions.js
        modified = WebProjectUtils.addJSToTag(docTagx, root, "loupe_js_url",
                "/resources/scripts/loupefield/loupe-functions.js") || modified;

        if (modified) {
            XmlUtils.writeXml(docTagxMutableFile.getOutputStream(), docTagx);
        }

    }

    private LogicalPath getWebappPath() {
        return WebProjectUtils.getWebappPath(projectOperations);
    }

    /**
     * Annotates given Controller with GvNIXLoupeController
     * 
     * @param controller
     */
    private void doAddControllerAnnotation(JavaType controller) {
        Validate.notNull(controller, "Controller required");

        // Getting current controller
        ClassOrInterfaceTypeDetails existingController = typeLocationService
                .getTypeDetails(controller);

        // Get @Controller annotation
        WebScaffoldAnnotationValues annotationValues = new WebScaffoldAnnotationValues(
                existingController);
        JavaType entity = annotationValues.getFormBackingObject();
        // Validating if is a controller
        Validate.notNull(entity, "Operation only supported for controllers");

        // Checking if is already annoted
        final boolean isAlreadyAnnotated = MemberFindingUtils
                .getAnnotationOfType(existingController.getAnnotations(),
                        ANNOTATION_LOUPE_CONTROLLER) != null;

        if (!isAlreadyAnnotated) {
            ClassOrInterfaceTypeDetailsBuilder classOrInterfaceTypeDetailsBuilder = new ClassOrInterfaceTypeDetailsBuilder(
                    existingController);

            AnnotationMetadataBuilder annotationBuilder = new AnnotationMetadataBuilder(
                    ANNOTATION_LOUPE_CONTROLLER);

            // Add annotation to target type
            classOrInterfaceTypeDetailsBuilder.addAnnotation(annotationBuilder
                    .build());

            // Save changes to disk
            typeManagementService
                    .createOrUpdateTypeOnDisk(classOrInterfaceTypeDetailsBuilder
                            .build());
        }

    }

    /**
     * Update project pom: install repositories and dependencies
     */
    private void setupProjectPom() {
        // Get add-on configuration file
        Element configuration = XmlUtils.getConfiguration(getClass());

        // Install the add-on repository needed
        List<Element> repos = XmlUtils.findElements(
                "/configuration/gvnix/repositories/repository", configuration);
        for (Element repo : repos) {
            projectOperations.addRepositories(
                    projectOperations.getFocusedModuleName(),
                    Collections.singleton(new Repository(repo)));
        }

        // Install properties
        List<Element> properties = XmlUtils.findElements(
                "/configuration/gvnix/properties/*", configuration);
        for (Element property : properties) {
            projectOperations.addProperty(projectOperations
                    .getFocusedModuleName(), new Property(property));
        }

        // Install dependencies
        List<Element> depens = XmlUtils.findElements(
                "/configuration/gvnix/dependencies/dependency", configuration);

        DependenciesVersionManager.manageDependencyVersion(metadataService,
                projectOperations, depens);
    }

    /***
     * FEATURE METHODS
     */

    @Override
    public String getName() {
        return FEATURE_NAME_GVNIX_LOUPEFIELDS;
    }

    @Override
    public boolean isInstalledInModule(String moduleName) {
        PathResolver pathResolver = projectOperations.getPathResolver();
        String dirPath = pathResolver.getIdentifier(getWebappPath(),
                "scripts/loupefield/loupe-functions.js");
        return fileManager.exists(dirPath);
    }
}