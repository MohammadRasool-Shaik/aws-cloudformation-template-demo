version = 0.1
[y]
[y.deploy]
[y.deploy.parameters]
stack_name = "API-Queue-Handler-Stack"
s3_bucket = "aws-sam-cli-managed-default-samclisourcebucket-1ijnbi80oelfs"
s3_prefix = "API-Queue-Handler-Stack"
region = "ap-south-1"
confirm_changeset = true
capabilities = "CAPABILITY_IAM"
disable_rollback = true
image_repositories = []
parameter_overrides = "Environment=\"SIT\" StandardQueueName=\"MyQueueForTrigger\""
