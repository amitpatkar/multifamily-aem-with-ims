const merge             = require('webpack-merge');
const common            = require('./webpack.common.js');
const path              = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');

const SOURCE_ROOT = __dirname + '/src/main/webpack';
const staticFiles = ['index.html', 
                    'header.html',
                    'section100.html', 
                    'section33-33-33.html', 
                    'get-in-touch.html', 
                    "hero.html",
                    "availability-feed.html",
                "section50-50.html",
            "accordions.html",
        "apartment-detail-optionA.html",
        "apartment-detail-optionB.html",
        "apartment-detail-optionC.html"];
const pluginsArr = [];
staticFiles.forEach(filename => {
    pluginsArr.push(new HtmlWebpackPlugin({
        filename: filename,
        template: path.resolve(__dirname, SOURCE_ROOT + '/static/' + filename),
    }))
});

module.exports = env => {
    const writeToDisk = env && Boolean(env.writeToDisk);

    return merge(common, {
        mode: 'development',
        devtool: 'inline-source-map',
        performance: { hints: 'warning' },
        plugins: pluginsArr,
        devServer: {
            inline: true,
            proxy: [{
                context: ['/content', '/etc.clientlibs'],
                target: 'http://localhost:4502',
            }],
            writeToDisk,
            liveReload: !writeToDisk
        }
    });
}