package ch.hevs.aipu.conference.backend;

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
        name = "newsApi",
        version = "v1",
        resource = "news",
        namespace = @ApiNamespace(
                ownerDomain = "backend.conference.aipu.hevs.ch",
                ownerName = "backend.conference.aipu.hevs.ch",
                packagePath = ""
        )
)
public class NewsEndpoint {

    private static final Logger logger = Logger.getLogger(NewsEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(News.class);
    }

    /**
     * Returns the {@link News} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code News} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "news/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public News get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting News with ID: " + id);
        News news = ofy().load().type(News.class).id(id).now();
        if (news == null) {
            throw new NotFoundException("Could not find News with ID: " + id);
        }
        return news;
    }

    /**
     * Inserts a new {@code News}.
     */
    @ApiMethod(
            name = "insert",
            path = "news",
            httpMethod = ApiMethod.HttpMethod.POST)
    public News insert(News news) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that news.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(news).now();
        logger.info("Created News with ID: " + news.getId());

        return ofy().load().entity(news).now();
    }

    /**
     * Updates an existing {@code News}.
     *
     * @param id   the ID of the entity to be updated
     * @param news the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code News}
     */
    @ApiMethod(
            name = "update",
            path = "news/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public News update(@Named("id") Long id, News news) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(news).now();
        logger.info("Updated News: " + news);
        return ofy().load().entity(news).now();
    }

    /**
     * Deletes the specified {@code News}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code News}
     */
    @ApiMethod(
            name = "remove",
            path = "news/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(News.class).id(id).now();
        logger.info("Deleted News with ID: " + id);
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
            path = "news",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<News> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<News> query = ofy().load().type(News.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<News> queryIterator = query.iterator();
        List<News> newsList = new ArrayList<News>(limit);
        while (queryIterator.hasNext()) {
            newsList.add(queryIterator.next());
        }
        return CollectionResponse.<News>builder().setItems(newsList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(News.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find News with ID: " + id);
        }
    }
}