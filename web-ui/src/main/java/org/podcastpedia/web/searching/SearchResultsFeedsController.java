package org.podcastpedia.web.searching;

import org.podcastpedia.common.domain.Episode;
import org.podcastpedia.common.domain.Podcast;
import org.podcastpedia.common.util.config.ConfigService;
import org.podcastpedia.core.searching.Result;
import org.podcastpedia.core.searching.SearchData;
import org.podcastpedia.core.searching.SearchResult;
import org.podcastpedia.core.searching.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class that maps to feeds service generation, for both the start
 * page feeds, and search results generated feeds.
 *
 * @author amasia
 *
 */
@Controller
@RequestMapping("/feeds/search")
public class SearchResultsFeedsController implements MessageSourceAware {

	private MessageSource messageSource;

	@Autowired
	private ConfigService configService;

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Autowired
	private SearchService searchService;


    /**
     * Returns list of episodes for the search criteria to be generated as a rss
     * feed. Request comes from results page from searching episodes.
     *
     * @param searchInput
     * @param model
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("results.rss")
    public String getResultsFromSearchRssFeed(
        @ModelAttribute("advancedSearchData") SearchData searchInput,
        Model model) throws UnsupportedEncodingException {

        searchInput.setForFeed(true);
        List<Result> results = new ArrayList<Result>();

        SearchResult episodesFromSearchCriteria = searchService
            .getResultsForSearchCriteria(searchInput);

        results = episodesFromSearchCriteria.getResults();

        model.addAttribute("list_of_results", results);
        model.addAttribute("feed_title", messageSource.getMessage(
            "search_results.feed_title", null,
            LocaleContextHolder.getLocale()));
        model.addAttribute("feed_description", messageSource.getMessage(
            "search_results.feed_description", null,
            LocaleContextHolder.getLocale()));
        // set link to search results for data - get it through getPath
        model.addAttribute("feed_link",
            configService.getValue("HOST_AND_PORT_URL"));
        model.addAttribute("HOST_AND_PORT_URL",
            configService.getValue("HOST_AND_PORT_URL"));

        return "searchResultsPageRssFeedView";
    }

    /**
     * Returns list of episodes for the search criteria to be generated as a
     * atom feed. Request comes from results page for episodes.
     *
     * @param searchInput
     * @param model
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("results.atom")
    public String getResultsFromSearchAtomFeed(
        @ModelAttribute("advancedSearchData") SearchData searchInput,
        Model model) throws UnsupportedEncodingException {

        searchInput.setForFeed(true);

        List<Result> results = new ArrayList<Result>();

        SearchResult resultsFromSearchCriteria = searchService
            .getResultsForSearchCriteria(searchInput);
        results = resultsFromSearchCriteria.getResults();

        model.addAttribute("list_of_results", results);
        model.addAttribute("feed_id",
            "tags:podcastpedia.org,2013-04-30:found-episodes");
        model.addAttribute("feed_title", messageSource.getMessage(
            "search_results.feed_title", null,
            LocaleContextHolder.getLocale()));
        model.addAttribute("feed_description", messageSource.getMessage(
            "search_results.feed_description", null,
            LocaleContextHolder.getLocale()));
        // set link to search results for data - get it through getPath
        model.addAttribute("feed_link",
            configService.getValue("HOST_AND_PORT_URL"));
        model.addAttribute("HOST_AND_PORT_URL",
            configService.getValue("HOST_AND_PORT_URL"));

        return "searchResultsPageAtomFeedView";
    }

}
