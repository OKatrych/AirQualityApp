The project was build in a few hours so there are a lot of not polished things:

1. DataStore is used for DB - ideally should be some relational DB
2. UI looks ugly ;-)
3. No pagination support
4. Hardcoded API key
5. Big image screen lacks caching

## NOTE: 
1. kotlin.collections.* are `@Stable` in this project with the help of `compose_compiler_config.conf` file
2. Please use release build type when testing to enable compose optimizations