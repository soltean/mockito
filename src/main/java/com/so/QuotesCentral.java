package com.so;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class QuotesCentral {

    private MessageServer messageServer;
    private QuoteProvider quoteProvider;

    public List<Quote> getQuotes() {
        return quoteProvider.generateQuotes();
    }

    public List<Quote> findByAuthor(final String author) {
        return getQuotes().stream().filter(q -> q.getAuthor().equals(author)).collect(Collectors.toList());
    }

    public boolean publishQuotes(List<Quote> quotes) {
        try {
            messageServer.connect();
            return getMessageServer().publish(quotes);
        } finally {
            messageServer.disconnect();
        }
    }

    public boolean publishQuotesByAuthor(String name) {
        return publishQuotes(findByAuthor(name));
    }

}
