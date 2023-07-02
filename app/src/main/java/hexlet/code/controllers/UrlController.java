package hexlet.code.controllers;

import hexlet.code.models.Url;
import hexlet.code.models.UrlCheck;
import hexlet.code.models.query.QUrl;
import io.ebean.PagedList;
import io.javalin.http.Handler;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class UrlController {

    public static Handler createUrl = ctx -> {
        URL receivedUrl;
        String formatedUrl;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            receivedUrl = new URL(ctx.formParam("url"));
        } catch (MalformedURLException e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flash-type", "warning");
            ctx.redirect("/");
            return;
        }

        if (receivedUrl.getPort() == -1) {
            formatedUrl = stringBuilder.append(receivedUrl.getProtocol())
                    .append("://")
                    .append(receivedUrl.getHost())
                    .toString();
        } else {
            formatedUrl = stringBuilder.append(receivedUrl.getProtocol())
                    .append("://")
                    .append(receivedUrl.getHost())
                    .append(":")
                    .append(receivedUrl.getPort())
                    .toString();
        }

        Url url = new QUrl()
            .name.equalTo(formatedUrl)
            .findOne();

        if (url == null) {
            Url newUrl = new Url(formatedUrl);
            newUrl.save();
            ctx.sessionAttribute("flash", "Страница успешно добавлена");
            ctx.sessionAttribute("flash-type", "success");
        } else {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.sessionAttribute("flash-type", "warning");
        }

        ctx.redirect("/urls");
    };

    public static Handler listUrls = ctx -> {
        int page = ctx.queryParamAsClass("page", Integer.class).getOrDefault(1);
        int rowsPerPage = 10;
        int offset = (page - 1) * rowsPerPage;

        PagedList<Url> pagedUrls = new QUrl()
                .setFirstRow(offset)
                .setMaxRows(rowsPerPage)
                .orderBy()
                    .id.asc()
                .findPagedList();

        List<Url> urls = pagedUrls.getList();
        int totalPages = pagedUrls.getTotalPageCount();

        ctx.attribute("urls", urls);
        ctx.attribute("totalPages", totalPages);
        ctx.attribute("page", page);
        ctx.render("urls/index.html");
    };

    public static Handler showUrl = ctx -> {
        long currentId = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);

        Url url = new QUrl()
                .id.equalTo(currentId)
                .findOne();

        url.getUrlChecks().forEach(System.out::println);

        ctx.attribute("url", url);
        ctx.render("urls/show.html");
    };

    public static Handler createCheck = ctx -> {
        int urlId = Integer.parseInt(ctx.path().split("/")[2]);
        Url url = new QUrl()
                .id.equalTo(urlId)
                .findOne();

        if (url != null) {
            String requestUrl = url.getName();
            HttpResponse<String> responseGet = Unirest.get(requestUrl).asString();
            int status = responseGet.getStatus();
            UrlCheck urlCheck = new UrlCheck(status, "title-1", "header-1", "description-1", url);
            urlCheck.save();

            ctx.attribute("urlCheck", urlCheck);
            ctx.sessionAttribute("flash", "Страница успешно проверена");
            ctx.sessionAttribute("flash-type", "success");
            ctx.redirect("/urls/" + urlId);
        } else {
            ctx.sessionAttribute("flash", "Некорректный адрес");
            ctx.sessionAttribute("flash-type", "warning");
            ctx.render("urls/show.html");
        }
    };
}
