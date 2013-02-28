package com.moandjiezana.toml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.moandjiezana.toml.testutils.KeyGroupAsMap;
import com.moandjiezana.toml.testutils.TomlKeyGroups;
import com.moandjiezana.toml.testutils.TomlPrimitives;

import java.io.File;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

public class TomlToClassTest {

  @Test
  public void should_convert_primitive_values() throws Exception {
    Toml toml = new Toml().parse(file("should_convert_primitive_values.toml"));

    TomlPrimitives values = toml.to(TomlPrimitives.class);

    Calendar calendar = Calendar.getInstance();
    calendar.set(1979, Calendar.MAY, 27, 7, 32, 00);
    calendar.set(Calendar.MILLISECOND, 0);
    calendar.setTimeZone(TimeZone.getTimeZone("UTC"));

    assertEquals("string", values.string);
    assertEquals((Long) 123L, values.number);
    assertEquals(2.1, values.decimal, 0);
    assertTrue(values.bool);
    assertEquals(calendar.getTime(), values.date);
  }

  @Test
  public void should_convert_key_groups() throws Exception {
    String fileName = "should_convert_key_groups.toml";
    Toml toml = new Toml().parse(file(fileName));

    TomlKeyGroups tomlKeyGroups = toml.to(TomlKeyGroups.class);

    assertEquals("value1", tomlKeyGroups.group1.string);
    assertEquals("value2", tomlKeyGroups.group2.string);
  }

  @Test
  public void should_use_defaults() throws Exception {
    Toml defaults = new Toml().parse(file("should_convert_key_groups.toml"));
    Toml toml = new Toml(defaults).parse("");

    TomlKeyGroups tomlKeyGroups = toml.to(TomlKeyGroups.class);

    assertEquals("value1", tomlKeyGroups.group1.string);
    assertEquals("value2", tomlKeyGroups.group2.string);
  }

  @Test
  public void should_ignore_keys_not_in_class() throws Exception {
    TomlPrimitives tomlPrimitives = new Toml().parse("a=1\nstring=\"s\"").to(TomlPrimitives.class);

    assertEquals("s", tomlPrimitives.string);
  }

  @Test
  public void should_convert_key_group_as_map() throws Exception {
    KeyGroupAsMap keyGroupAsMap = new Toml().parse("[group]\nkey=\"value\"").to(KeyGroupAsMap.class);

    assertEquals("value", keyGroupAsMap.group.get("key"));
  }

  private File file(String fileName) {
    return new File(getClass().getResource(fileName).getFile());
  }
}