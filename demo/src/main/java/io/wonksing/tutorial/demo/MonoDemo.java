package io.wonksing.tutorial.demo;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.apache.logging.log4j.util.Strings;

import reactor.core.publisher.Mono;

public class MonoDemo {
    
    void just() {
        Mono<String> m = Mono.just("StringValue");
        m.subscribe(v -> {
            System.out.println(v);
        });
    }

    void justOrEmpty() {
        String a = null;
        Mono<String> m = Mono.justOrEmpty(a);
        m.subscribe(v -> {
            System.out.println(a);
        });
    }

    void switchIfEmpty() {
        String a = null;
        Mono<String> m = Mono.justOrEmpty(a)
            .switchIfEmpty(Mono.defer(() -> Mono.just("hoy")));

        m.subscribe(v -> {
            System.out.println(v);
        });
    }

    void fromSupplier() {
        Mono<String> m = Mono.fromSupplier(() -> {
            String v = "";
            v = Strings.concat(v, "fromSupplier");
            return v;
        });

        m.subscribe(v -> {
            System.out.println(v);
        });
    }

    void error() {
        Mono<Mono<String>> m = Mono.fromSupplier(()->{
            
            return Mono.just(null);
        });
        m.subscribe(v -> {
            System.out.println(v);
        });
    }

    void onErrorResume() {
        Mono.fromSupplier(()->{
            Mono.just(null);
            return "";
        }).onErrorResume(e -> {
            return Mono.just("resumed");
        }).subscribe(v -> {
            System.out.println(v);
        });
    }

    void onErrorReturn() {
        // List<Integer> a = null;
        var a = Arrays.asList(1, 2, 3);
        Mono<Integer> m = Mono.justOrEmpty(0)
            .switchIfEmpty(Mono.defer(()->Mono.just(null)))
            .onErrorReturn(0);

        m.subscribe(v -> {
            System.out.println(v);
        });
    }

    void map() {
        
        List<Integer> a = Arrays.asList(1, 2, 3);
        Mono<String> m = Mono.just(a)
            .map(v -> {
                return String.valueOf(v.get(0));
            });
        m.subscribe(v -> {
            System.out.println(v);
        });
    }

    void flatMap() {
        String[] a = new String[]{"1", "2", "3", "4", "5"};
        
        var m = Mono.just(a).flatMap(v -> {
            return Mono.just(v.length);
        });

        m.subscribe(v->{
            System.out.println(v);
        });
    }
}
