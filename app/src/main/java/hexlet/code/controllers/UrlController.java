package hexlet.code.controllers;

import hexlet.code.models.Url;
import hexlet.code.models.query.QUrl;
import io.ebean.PagedList;
import io.javalin.http.Handler;

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
        } else {
            ctx.sessionAttribute("flash", "Страница уже существует");
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

        ctx.attribute("url", url);
        ctx.render("urls/show.html");
    };
}
