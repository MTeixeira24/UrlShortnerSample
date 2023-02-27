package pt.teixeiram2.UrlShortner.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import pt.teixeiram2.UrlShortner.model.UrlMap;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ShortUrlGeneratorTest {

    private static final String HUGE_URL = """
            https://awesome.saas.service/fetch?blobQueryParam=
            MqTEJ9sdbTsHgd8koBeK
            BR6VVpnOlJwv71J2rgN0
            RwqQEffy3wWYcr3lHFTI
            zThKdNe4BGuhgBiQcVik
            HN8APQ6hm3Xnz1ykoSnn
            jTt3NqTjGkhtJDeSGhAJ
            WOZvSKt5OsAtgaZ5ubb5
            dsmGVKLimPtAn9a0SGSX
            Wzd8IlqP7agSWmDOYY4Q
            nzfKn7mIgEwpLdJxxV2q
            P6F4iI06vNPLbiZPZwNp
            GQpB0hRQIpITJCyWlHCs
            vZOtApXp03iuujiNaHOf
            KB90BX0Mierktwp975UM
            RHbLSMPuhWkfBDU48C4Z
            XUIwBaWl2z03PeBeRLaC
            Ov9T7BwBcBmkXiG1tm0B
            5xt2ZP3xbWA63BEvJ32k
            EaKHY0wQ9JPfyXERYhe5
            jD80XBuEUw9jItZXetcl
            98BCJoYkmnqKuT7Z3MAP
            6WnPzQgqyYOPuJMTVI9k
            79WKoZBPUyAcvWBMOasY
            vdgM4RDMqaf6fm8Gsnge
            OOCC5Ui0IxnxkGoFsfso
            BUVsjuGbi3aiebjrFwH3
            iLfaKineiZChiiirupuB
            m7CNnmKToE95kQetnJXg
            ttyFs42PDKIGpwOfZlxp
            8MKxH9dyDoJMQQR8PEIA
            p5uEQHzHBj5J6ZgLvmUm
            etyoeX3w5DeiNwZw4NiG
            lACbIUkAJ4RTB2AtWtU4
            xIJU6qIrHypyviicUjH4
            ouLmIZY8c0XewmLPGlLe
            dpwwFXfifuYjkTISkeo9
            bmJqmDRQsbgTtoREIJJ0
            sSxamKAFsejGb80f4jt4
            45ZuDfb0139Fgki5R9b8
            vjq3fVBQzYKeq6asp0b0
            5QiA6Fq582Gb5s5mzbee
            Aq6kJHDqXeTSa8Qvtkt0
            tZZtm7szARJQUKpNR6uA
            0zNtbEmAgonwFI94OmTW
            dHhGkPA6Rd0G4CPfzEEt
            H83TrYo3LibWQBPBtpNV
            wbLY9OlLVD3pyB2rgnV9
            owBzdyyzbPAnYxTJj0jP
            hwbWIByijWPd5UCX7cEp
            ATAPhOIn4sxWaxoaMOt8
            piwga0zlUYcKhrpYOzgJ
            9TAvuC42J768DP8jEgZx
            aAeu70shcTSw3S6MIWKG
            q6IlOda1TdVRkSOTMj0U
            8Q5MFdjqOcBNjUKioMDk
            LvdadoqdTIdiSc7W5Mfw
            iXdkFbZSrn50q3HTRqrT
            mH6MZgOORZbPt5UEZszf
            w5r6X5UtcrcJCVCnrigX
            ugNz26avovaOQWYZvY7Z
            Of4JxkBK8bK7mOTazUp8
            2kyfASLiOFZPtvZzcnkp
            oOoXM0i6Xwvl778QWezA
            islYVPS61mXASg5zwHjf
            QbLmw57uwzXWQiuKjnHs
            Jng3AYZCVRjoHnkmpPk5
            qDJa8U39zHiMymlWnrVC
            NqeX1IPsKIBy3F3OzyTU
            uWfeJznnXjvQ6RhVKSP2
            FVLSi1RZsl3LFxAOarB6
            ryZG8kMUA3kuMf0780C6
            X6Nw5YyiK7IXfBh6xZgj
            uerAwcwfa3nwLA9cFSUl
            hU1W68V06ZVXFP8mhPR9
            f0xkT7jauES4uw4VWJch
            5tgs5Vro36vD2aUO5uek
            79D4XxyRtULXZxPqKGFw
            yVWypBzLd5kxgqchzJW2
            edEmPTq3mNUxNUpNkCIN
            BLsZRCwXCMANqecs8aQX
            JOPeT1tcD1twkS2GuJVr
            N10csTO7quiL146Nokg0
            QWx1hoe1BQtjNsPEIFN5
            9MsryH8OYCidMGdizJtO
            VJ4OWjcLTIQdQFjXLhqm
            ztaJrOYZreeZoxAch8mD
            26U1bKtScxgtKALrDync
            powLV21CqqmbjznWGmE9
            siFChkkAQQKdJPFICDoq
            ElVculJXlyJd8ipQv9Dc
            m6Rtzf5QGStWqvj0RMV4
            11oxeJjUHsfaG0pyhKYZ
            KTmgPTocspQYKbrnrLpo
            LudXFrNyVRLlY3jJ9pX2
            Ym5c36EiAPcFhcYqkm3R
            GtqWb5m2wPClgGjOY3TW
            bblXIjsCvboNqL4vO0OB
            8bPlveOg6Q2Ax7W2dCWo
            PthYDrTZzRRKghp9QRKb
            Eoqp30MTWF5d3XyXQSnU
            OHS0uV3JIHM8r3tVJYao
            54yV8PZ9vFP90LmyD4k5
            8hzAMSEbltUD22aqyg9e
            rsbQ9MNAX4UL0cP6d6RR
            4wSQfxLDTaOdZVfs9pXl
            UtsR5EFGBayhcRHJlnxD
            F5EiNqmnkdJ7e9fEF1Wt
            NOObVoahVHsoYtrDgAt3
            83HmBMgAsqSzclne9xoQ
            DTOwBWZzTynyBKFeGlkz
            iMV1eISJrLYcMruVGMcF
            zOaF5qASvsr2tYJbr2FL
            OSMOrlklngzjsZZDIsPP
            4qsnp5WfhWVMK5oypuie
            XVRZEh9BA82HPs2MWkpX
            RGCZVGSHYSxBRu3xgIZJ
            ExuVuZ2vBGla5uc9PFUy
            wgRhvv2vHVqwMytsdWF2
            XxvOwCHAoxlaCSlFhboj
            mQc2AJeGHxBjalNwMZB2
            KDeVzP7S1iSCFWfFMwIq
            HtsBBuSYo42vlTvIjgZ3
            4poduYONMdX9DWclpkSl
            gcjgl3VMDwoV4S7MwHCH
            dewXVDB6MyZydVIWeIgW
            v49HNXGVXoytoHjWzFub
            SGCR7ykafv89bWphna0W
            Qtkx7YmwXpeJtuDH9cWP
            X6rlETURk0IOOueMbDuq
            iFykdLuO9vIuMQF9STHD
            D3V8BVgPkaZ2ia9acQ0d
            IRNIED0iCyWDD4Yp0kk3
            YW7DZR5g6GJt9mm9ddtD
            Yg5nfvDsrOpJ8LdI8eWF
            iTNhYrzp1SJW1HLh7gIl
            q78BVYJK4PrpvKtT6z0G
            PjpvDk71alGYV2w6ElsZ
            Ceii3J6r4WotYn3W88bO
            DJa87VJXNblyx8fM7iqb
            QhN35G2Q9Enu7fcsHtf3
            V7Luodg3oKSwUvCgh0jr
            3IuT4MsTLQ5dXMJsOuGt
            X4MUDagV3kRgkB7vbnUf
            0IkT49AiCsAdF0ZgBMXN
            AzBPwH0sHfZMYEC9jRjB
            aJp6cRRU6arIrAuyfNdx
            x8ZvPvucP3SWkp2P5ONp
            vhWp4YhAiIITiXpZp4ix
            JtCr4dRqxupoHCLhgFt2
            r7mqNxlNjGOoHaB8Cioi
            """;
    private ShortUrlGenerator victim;

    public static Stream<Arguments> urlArgProvider() {
        return Stream.of(
                Arguments.of("www.example.com/path?myQuery=value#", "f0plg58"),
                Arguments.of("http://www.example.com/path?key=value#", "3wslt8v"),
                Arguments.of("http://www.example.com/path?key=myValue#", "3wslt57"),
                Arguments.of("http://www.xeample.com/path?key=myValue#", "3j8lt57"),
                Arguments.of(HUGE_URL, "le06eq7"),
                Arguments.of(HUGE_URL.replaceFirst("dsm", "abc"), "8e06eq7")
        );
    }

    public static Stream<Arguments> checksumArgProvider() {
        return Stream.of(
                Arguments.of("www.example.com/path?myQuery=value#", -7749012243208215097L),
                Arguments.of("http://www.example.com/path?key=value#", -6035612812965332595L),
                Arguments.of("http://www.example.com/path?key=myValue#", -2636556464829794186L),
                Arguments.of("http://www.xeample.com/path?key=myValue#", -2636540782639750916L),
                Arguments.of(HUGE_URL, 8977956589421930356L),
                Arguments.of(HUGE_URL.replaceFirst("dsm", "abc"), -8087248073619821488L)
        );
    }

    @BeforeEach
    void setUp() {
        victim = new ShortUrlGenerator();
    }

    @ParameterizedTest
    @MethodSource("urlArgProvider")
    void shouldShortenUrl(String fullUrl, String expectedShortUrl) {
        UrlMap result = victim.shortenUrl(fullUrl, -7749012243208215097L);
        assertEquals(expectedShortUrl, result.getShortUrl());
        assertEquals(fullUrl, result.getUrl());
        assertEquals(-7749012243208215097L, result.getChecksum());
    }

    @ParameterizedTest
    @MethodSource("checksumArgProvider")
    void shouldComputeChecksum(String fullUrl, long expectedChecksum) {
        long checksum = victim.computeChecksum(fullUrl);
        assertEquals(expectedChecksum, checksum);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowOnIllegalArgument(String arg) {
        assertThrows(IllegalArgumentException.class, () -> victim.shortenUrl(arg, 0L));
    }

}