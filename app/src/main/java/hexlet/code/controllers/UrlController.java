package hexlet.code.controllers;

import hexlet.code.models.Url;
import hexlet.code.models.query.QUrl;
import io.ebeaninternal.server.util.Str;
import io.javalin.http.Handler;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlController {

    public static Handler createUrl = ctx -> {
        URL receivedUrl;
        String formatedUrl;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            receivedUrl = new URL(ctx.formParam("url"));
        } catch (MalformedURLException e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.render("index.html");
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

        ctx.render("index.html");
    };
}
