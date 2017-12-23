
path = require 'path'
webpack = require 'webpack'
resolve = require('path').resolve

module.exports =
  entry:
    main: './entry/dev'
  devServer:
    clientLogLevel: 'info'
    stats: 'errors-only'
    contentBase: resolve(__dirname, 'dist/')
    publicPath: '/'
    host: '0.0.0.0'
  output:
    filename: '[name].js'
  module:
    rules: [
      test: /\.css$/
      loaders: ['style-loader', 'css-loader']
    ,
      test: /\.(eot|svg|ttf|woff2?)(\?.+)?$/
      loader: 'url-loader'
      query:
        limit: 100
        name: 'fonts/[name].[ext]'
    ,
      test: /\.(md|cljs)$/, use: 'raw-loader'
    ]
  plugins: [
    new webpack.HotModuleReplacementPlugin()
    new webpack.NamedModulesPlugin()
  ]