# How to execute tests

To execute the tests for this project, execute the following command in your terminal:

```bash
docker build -t remirada-testing .
docker run --rm --env-file .env remirada-testing
```