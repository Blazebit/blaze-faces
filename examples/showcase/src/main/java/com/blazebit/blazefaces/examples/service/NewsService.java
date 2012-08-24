package com.blazebit.blazefaces.examples.service;

import java.util.List;

import com.blazebit.blazefaces.examples.domain.NewsGroup;

public interface NewsService {

	public List<NewsGroup> fetchNews();
}
