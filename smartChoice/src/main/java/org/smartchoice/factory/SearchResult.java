package org.smartchoice.factory;

import org.smartchoice.dto.ProductDto;

import java.util.List;

public interface SearchResult {
    List<ProductDto> getProduct(Object o);
}
