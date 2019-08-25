package mflix.lessons.utils;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

public class StringObjectIdCodec implements Codec<String> {

  @Override
  public void encode(
      final BsonWriter writer, final String value, final EncoderContext encoderContext) {
    writer.writeObjectId(new ObjectId(value));
  }

  @Override
  public Class<String> getEncoderClass() {
    return String.class;
  }

  @Override
  public String decode(final BsonReader reader, final DecoderContext decoderContext) {
    return reader.readObjectId().toHexString();
  }
}
