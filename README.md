# URL Shortner Sample

Sample URL Shortner application that leverages modern Spring testing approaches and technologies


## Troubleshooting

### Test containers fails with a "Could not find a valid Docker environment" error.

Recent versions of docker desktop do not create the `docker.sock` socket file in `/var/run` which is where most applications
(mvn or intellij) will look for a docker daemon.
To fix this, symlink the created socket file in the user file to `var/run`

```bash
sudo ln -s ${path_to_user_home_dir}/.docker/desktop/docker.sock /var/run/docker.sock
```
