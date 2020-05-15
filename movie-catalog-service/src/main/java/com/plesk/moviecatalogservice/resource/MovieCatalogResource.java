package com.plesk.moviecatalogservice.resource;

import com.plesk.moviecatalogservice.model.CatalogueItem;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @RequestMapping("/{userId}")
    public List<CatalogueItem> getCatalog(@PathVariable ("userId") String userId) {
        return Collections.singletonList(
           new CatalogueItem("Transformers", "Test", 4)
        );
    }
}
