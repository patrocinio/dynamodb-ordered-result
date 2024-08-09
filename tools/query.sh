aws dynamodb get-item \
    --table-name ordered_result \
    --key '{"key":{"N":"0"}, "seq_no":{"N":"0"}}'