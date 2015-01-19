package com.richjavalabs.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p/>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "codeItemApi",
        version = "v1",
        resource = "codeItem",
        namespace = @ApiNamespace(
                ownerDomain = "backend.richjavalabs.com",
                ownerName = "backend.richjavalabs.com",
                packagePath = ""
        )
)
public class CodeItemEndpoint {

    private static final Logger logger = Logger.getLogger(CodeItemEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(CodeItem.class);
    }

    /**
     * Returns the {@link CodeItem} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code CodeItem} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "codeItem/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CodeItem get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting CodeItem with ID: " + id);
        CodeItem codeItem = ofy().load().type(CodeItem.class).id(id).now();
        if (codeItem == null) {
            throw new NotFoundException("Could not find CodeItem with ID: " + id);
        }
        return codeItem;
    }

    /**
     * Inserts a new {@code CodeItem}.
     */
    @ApiMethod(
            name = "insert",
            path = "codeItem",
            httpMethod = ApiMethod.HttpMethod.POST)
    public CodeItem insert(CodeItem codeItem) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that codeItem.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(codeItem).now();
        logger.info("Created CodeItem with ID: " + codeItem.getId());

        return ofy().load().entity(codeItem).now();
    }

    /**
     * Updates an existing {@code CodeItem}.
     *
     * @param id       the ID of the entity to be updated
     * @param codeItem the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code CodeItem}
     */
    @ApiMethod(
            name = "update",
            path = "codeItem/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public CodeItem update(@Named("id") Long id, CodeItem codeItem) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(codeItem).now();
        logger.info("Updated CodeItem: " + codeItem);
        return ofy().load().entity(codeItem).now();
    }

    /**
     * Deletes the specified {@code CodeItem}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code CodeItem}
     */
    @ApiMethod(
            name = "remove",
            path = "codeItem/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(CodeItem.class).id(id).now();
        logger.info("Deleted CodeItem with ID: " + id);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "list",
            path = "codeItem",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<CodeItem> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<CodeItem> query = ofy().load().type(CodeItem.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<CodeItem> queryIterator = query.iterator();
        List<CodeItem> codeItemList = new ArrayList<CodeItem>(limit);
        while (queryIterator.hasNext()) {
            codeItemList.add(queryIterator.next());
        }
        return CollectionResponse.<CodeItem>builder().setItems(codeItemList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(CodeItem.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find CodeItem with ID: " + id);
        }
    }
}