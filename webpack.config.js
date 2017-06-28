var path = require('path');
var webpack = require('webpack');
var ManifestPlugin = require('webpack-manifest-plugin');
var ExtractTextPlugin = require('extract-text-webpack-plugin');

module.exports = {
  entry: {
    main: './entry/page',
    vendor: [
      './target/cljs.core',
      './target/respo.core',
      './target/respo_ui.style'
    ]
  },
  output: {
    path: path.join(__dirname, './dist/'),
    filename: '[name].[chunkhash:8].js'
  },
  module: {
    rules: [
      {
        test: /\.css$/,
        loader: ExtractTextPlugin.extract({
          fallback: 'style-loader',
          use: 'css-loader'
        })
      },
      {
        test: /\.(eot|svg|ttf|woff2?)(\?.+)?$/,
        loader: 'url-loader',
        query: {
          limit: 100,
          name: 'fonts/[hash:8].[ext]'
        }
      },
      { test: /\.(md|cljs)$/, use: 'raw-loader' }
    ]
  },
  plugins: [
    new ExtractTextPlugin('[name].[chunkhash:8].css'),
    new webpack.optimize.CommonsChunkPlugin({
      name: 'vendor',
      filename: 'vendor.[chunkhash:8].js'
    }),
    new ManifestPlugin()
  ]
};