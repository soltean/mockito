package com.so;

import java.util.List;
import java.util.stream.Collectors;

public class QuotesCentral {

    private MessageServer messageServer;
    private QuoteProvider quoteProvider;

    public void setMessageServer(MessageServer messageServer) {
        this.messageServer = messageServer;
    }

    public void setQuoteProvider(QuoteProvider quoteProvider) {
        this.quoteProvider = quoteProvider;
    }

    private MessageServer getMessageServer() {
        return messageServer;
    }

    private QuoteProvider getProvider() {
        return quoteProvider;
    }

    public List<Quote> getQuotes() {
        return getProvider().generateQuotes();
    }

    public List<Quote> findByAuthor(final String author) {
        return getQuotes().stream().filter(q -> q.getAuthor().equals(author)).collect(Collectors.toList());
    }

    public boolean publishQuotes(List<Quote> quotes) {
        try {
            getMessageServer().connect();
            return getMessageServer().publish(quotes);
        } finally {
            getMessageServer().disconnect();
        }
    }

    public boolean publishQuotesByAuthor(String name) {
        return publishQuotes(findByAuthor(name));
    }

}
