package ru.job4j.threads.cache;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class Cache {

	private final Map<Integer, Base> memory = new ConcurrentHashMap<>();

	/**
	 * Добавляет новую модель в кэш, если модель с таким идентификатором еще не существует.
	 *
	 * @param model Модель для добавления в кэш.
	 * @return true, если модель была добавлена в кэш,
	 * и false, если модель с таким идентификатором уже существует.
	 */
	public boolean add(Base model) {
		return memory.putIfAbsent(model.id(), model) == null;
	}

	/**
	 * Обновляет модель в кэше, если версия модели в кэше совпадает с версией, передаваемой в метод.
	 * Если версии не совпадают, выбрасывается исключение.
	 *
	 * @param model Модель с новыми данными и версией для обновления.
	 * @return true, если модель была обновлена, и false, если модель не существовала в кэше.
	 * @throws OptimisticException если версия модели в кэше не совпадает с версией,
	 * передаваемой в метод.
	 */
	public boolean update(Base model) throws OptimisticException {
		Base stored = memory.computeIfPresent(model.id(), (key, base) -> {
				if (base.version() != model.version()) {
					throw new OptimisticException("Versions are not equal");
				}
				return new Base(base.id(), model.name(), base.version() + 1);
		});
		return stored != null;
	}

	/**
	 * Удаляет модель из кэша по ее идентификатору.
	 *
	 * @param id Идентификатор модели для удаления.
	 */
	public void delete(int id) {
		memory.remove(id);
	}

	/**
	 * Находит модель в кэше по ее идентификатору.
	 *
	 * @param id Идентификатор модели для поиска.
	 * @return Optional, содержащий модель, если она найдена,
	 * или пустой Optional, если модель не найдена.
	 */
	public Optional<Base> findById(int id) {
		return Stream.of(memory.get(id))
			.filter(Objects::nonNull)
			.findFirst();
	}
}