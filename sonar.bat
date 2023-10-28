mvn clean verify sonar:sonar \
  -Dsonar.login=admin \
  -Dsonar.projectKey=RangerFichier \
  -Dsonar.projectName='RangerFichier' \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=sqp_8659f4bc4970c5445b125175476b54ca6d446306