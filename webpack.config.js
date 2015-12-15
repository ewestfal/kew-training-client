var path = require('path');

module.exports = {
    entry: {
      javascript: "./src/main/resources/static/js/index.js",
      html: "./src/main/resources/static/index.html"
    },
    output: {
        path: path.resolve('./target/classes/static'),
        filename: "bundle.js",
        publicPath: "/"
    },
    module: {
        loaders: [
            { test: /\.js?$/, exclude: /(node_modules)/, loaders: ['react-hot', 'babel-loader?presets[]=es2015,presets[]=stage-0,presets[]=react'] },
            { test: /\.html$/, loader: "file?name=[name].[ext]", },
            { test: /\.css$/, loader: "style!css" },
            { test: /\.less$/, loader: "style!css!less" },
            { test: /\.(woff|woff2)(\?v=[0-9]\.[0-9]\.[0-9])?$/,  loader: "url-loader?limit=10000&mimetype=application/font-woff" },
            { test: /\.ttf(\?v=[0-9]\.[0-9]\.[0-9])?$/, loader: "file-loader" },
            { test: /\.eot(\?v=[0-9]\.[0-9]\.[0-9])?$/, loader: "file-loader" },
            { test: /\.svg(\?v=[0-9]\.[0-9]\.[0-9])?$/, loader: "file-loader" }
        ]
    }
};